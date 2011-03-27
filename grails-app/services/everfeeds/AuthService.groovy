package everfeeds

import org.scribe.builder.ServiceBuilder
import org.scribe.oauth.OAuthService
import org.scribe.model.*
import grails.converters.deep.JSON

class AuthService {

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    static transactional = true

    OAuthService getOAuthService(config, String callbackAction=null) {
        ServiceBuilder builder = new ServiceBuilder()
        builder.provider(config.provider)
        builder.apiKey(config.key.toString())
        builder.apiSecret(config.secret.toString())
        if(callbackAction) {
            builder.callback(g.createLink(controller:"access", action:callbackAction, absolute:true).toString())
        }
        if(config.scope) {
            builder.scope(config.scope.toString())
        }

        builder.build()
    }

    Response oAuthCall(String url, def config, String token, String secret, Verb method = Verb.GET){
        OAuthService service = getOAuthService(config)
        OAuthRequest request = new OAuthRequest(method, url);
        def tkn = new Token(token, secret)
        service.signRequest(tkn, request);
        request.send();
    }

    Object oAuthCallJson(String url, def config, String token, String secret, Verb method = Verb.GET) {
        JSON.parse(oAuthCall(url, config, token, secret, method).body)
    }

    Access getAccess(final String type, String screen, String token=null, String secret=null, String shard=null) {
        Access access = Access.findByIdentity(type + ":" + screen) ?: new Access(
                identity: type + ":" + screen,
                type: type
        )
        if(token) {
            access.token = token
            if(secret) access.secret = secret
            if(shard) access.shard = shard
            access.expired = false
        }
        access.save()
    }

    Access getAccess(Map params){
        if(!params.type || !params.screen) {
            throw new IllegalArgumentException("Cannot get access without type/screen")
        }

        Access access = Access.findByIdentity(params.type + ":" + params.screen) ?: new Access(
                identity: params.type + ":" + params.screen,
                type: params.type
        )
        if(params.token) {
            access.token = params.token
            if(params.secret) access.secret = params.secret
            if(params.shard) access.shard = params.shard
            access.expired = false
        }
        access.save()
    }

    void setAccountRole(Account account, String authority) {
        authority = authority.toUpperCase()
        AccountRole.create account, Role.findByAuthority(authority) ?: new Role(authority: authority).save()
    }
}
