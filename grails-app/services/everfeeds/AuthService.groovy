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
}
