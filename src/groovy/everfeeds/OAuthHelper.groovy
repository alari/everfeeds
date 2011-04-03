package everfeeds

import grails.converters.deep.JSON
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService

/**
 * Created by alari @ 03.04.11 13:13
 */
class OAuthHelper {
    static g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    static public String callApi(def oauthConfig, String url, String token, String secret, Verb method = Verb.GET) {
        OAuthService service = getOAuthService(oauthConfig)
        OAuthRequest request = new OAuthRequest(method, url);
        def tkn = new Token(token, secret)
        service.signRequest(tkn, request);
        return request.send()?.body;
    }

    static public Object callJsonApi(def oauthConfig, String url, String token, String secret, Verb method = Verb.GET) {
        JSON.parse(callApi(oauthConfig, url, token, secret, method))
    }

    static public OAuthService getOAuthService(oauthConfig, String accessType = null) {
        ServiceBuilder builder = new ServiceBuilder()
        builder.provider(oauthConfig.provider)
        builder.apiKey(oauthConfig.key.toString())
        builder.apiSecret(oauthConfig.secret.toString())
        if (accessType) {
            builder.callback(g.createLink(controller: "access", action: "callback", id: accessType, absolute: true).toString())
        }
        if (oauthConfig.scope) {
            builder.scope(oauthConfig.scope.toString())
        }

        builder.build()
    }
}
