package everfeeds.access

import grails.converters.deep.JSON
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService

/**
 * Created by alari @ 02.04.11 13:01
 */
/*abstract*/ class AOAuthAuth extends AAuth {
    static def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    //abstract static public Map authCallback(String verifierStr, Object session)

    static protected Map _authCallback(String verifierStr, Object session, String accessType, Closure closure) {
        System.err << "Session is ${session}"
        Verifier verifier = new Verifier(verifierStr);
        Token accessToken = session."${accessType}".service.getAccessToken(session."${accessType}".token, verifier);
        session."${accessType}" = null

        Map params = closure.call(accessToken)
        if (!params?.screen) {
            return null
        }
        params.type = accessType
        params.token = accessToken.token
        params.secret = accessToken.secret

        params
    }

    static public String getAuthUrl(def config, String accessType, Object session) {
        OAuthService service = getOAuthService(config.oauth, accessType)

        Token requestToken = service.getRequestToken();
        session."${accessType}" = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }

    static public String callApi(def config, String url, String token, String secret, Verb method = Verb.GET) {
        OAuthService service = getOAuthService(config)
        OAuthRequest request = new OAuthRequest(method, url);
        def tkn = new Token(token, secret)
        service.signRequest(tkn, request);
        return request.send()?.body;
    }

    static public Object callJsonApi(def config, String url, String token, String secret, Verb method = Verb.GET) {
        JSON.parse(callApi(config, url, token, secret, method))
    }

    static protected OAuthService getOAuthService(config, String accessType = null) {
        ServiceBuilder builder = new ServiceBuilder()
        builder.provider(config.provider)
        builder.apiKey(config.key.toString())
        builder.apiSecret(config.secret.toString())
        if (accessType) {
            builder.callback(g.createLink(controller: "access", action: "callback", id: accessType, absolute: true).toString())
        }
        if (config.scope) {
            builder.scope(config.scope.toString())
        }

        builder.build()
    }
}