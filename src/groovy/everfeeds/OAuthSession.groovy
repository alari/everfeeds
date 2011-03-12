package everfeeds

import oauth.signpost.basic.DefaultOAuthProvider
import oauth.signpost.basic.DefaultOAuthConsumer

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

/**
 * Created by alari @ 12.03.11 19:22
 */
class OAuthSession {
    private DefaultOAuthConsumer oauthConsumer
    private DefaultOAuthProvider oauthProvider

    private static ApplicationTagLib g = new ApplicationTagLib()

    OAuthSession(def config) {
        oauthConsumer = new DefaultOAuthConsumer(
                config.consumer.key, config.consumer.secret);

        oauthProvider = new DefaultOAuthProvider(
                config.requestTokenUrl,
                config.accessTokenUrl,
                config.authUrl);
    }

    String getAuthUrl(Map attrs) {
        attrs.absolute = true
        oauthProvider.retrieveRequestToken(
                oauthConsumer, g.createLink(attrs).toString());
    }

    void verify(String verifier) {
        oauthProvider.retrieveAccessToken(oauthConsumer, verifier);
    }

    String getToken() {
        oauthConsumer.getToken()
    }

    String getSecret() {
        oauthConsumer.getTokenSecret()
    }

    DefaultOAuthConsumer getConsumer() {
        oauthConsumer
    }

    DefaultOAuthProvider getProvider() {
        oauthProvider
    }
}
