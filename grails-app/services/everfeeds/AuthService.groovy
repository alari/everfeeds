package everfeeds

import grails.converters.deep.JSON
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.oauth.OAuthService
import org.springframework.web.context.request.RequestContextHolder
import org.scribe.model.Verifier

class AuthService {

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()
    def grailsApplication

    static transactional = true

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl(String accessType) {
        OAuthService service = getOAuthService(grailsApplication.config."${accessType}", "${accessType}Callback")

        Token requestToken = service.getRequestToken();
        session."${accessType}" = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }

    Access processCallback(String accessType, String verifierStr, Closure closure) {
        if(!session."${accessType}") {
            log.error "No session object related to ${accessType}"
            return null
        }

        Verifier verifier = new Verifier(verifierStr);
        Token accessToken = session."${accessType}".service.getAccessToken(session."${accessType}".token, verifier);
        session."${accessType}" = null

        Map params = closure.call(accessToken)
        if(!params?.screen) {
            log.debug "Seems like access denied: cannot figure out access screen name"
            return null
        }
        params.type = accessType
        params.token = accessToken.token
        params.secret = accessToken.secret

        getAccess(params)
    }

    OAuthService getOAuthService(config, String callbackAction = null) {
        ServiceBuilder builder = new ServiceBuilder()
        builder.provider(config.provider)
        builder.apiKey(config.key.toString())
        builder.apiSecret(config.secret.toString())
        if (callbackAction) {
            builder.callback(g.createLink(controller: "access", action: callbackAction, absolute: true).toString())
        }
        if (config.scope) {
            builder.scope(config.scope.toString())
        }

        builder.build()
    }

    Response oAuthCall(String url, def config, String token, String secret, Verb method = Verb.GET) {
        OAuthService service = getOAuthService(config)
        OAuthRequest request = new OAuthRequest(method, url);
        def tkn = new Token(token, secret)
        service.signRequest(tkn, request);
        request.send();
    }

    Object oAuthCallJson(String url, def config, String token, String secret, Verb method = Verb.GET) {
        JSON.parse(oAuthCall(url, config, token, secret, method).body)
    }

    Access getAccess(final String type, String screen, String token = null, String secret = null, String shard = null) {
        Access access = Access.findByIdentity(type + ":" + screen) ?: new Access(
                identity: type + ":" + screen,
                type: type
        )
        if (token) {
            access.token = token
            if (secret) access.secret = secret
            if (shard) access.shard = shard
            access.expired = false
        }
        access.save()
    }

    Access getAccess(Map params) {
        if (!params.type || !params.screen) {
            throw new IllegalArgumentException("Cannot get access without type/screen")
        }

        Access access = Access.findByIdentity(params.type + ":" + params.screen) ?: new Access(
                identity: params.type + ":" + params.screen,
                type: params.type
        )
        if (params.token) {
            access.token = params.token
            if (params.secret) access.secret = params.secret
            if (params.shard) access.shard = params.shard
            access.expired = false
        }
        access.save()
    }

    void setAccountRole(Account account, String authority) {
        authority = authority.toUpperCase()
        if (!authority.startsWith("ROLE_")) authority = "ROLE_" + authority
        AccountRole.create account, Role.getByAuthority(authority)
    }
}
