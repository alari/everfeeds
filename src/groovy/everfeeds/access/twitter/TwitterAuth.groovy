package everfeeds.access.twitter

import everfeeds.access.AOAuthAuth
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.scribe.model.Token
import everfeeds.OAuthHelper

/**
 * Created by alari @ 02.04.11 13:15
 */
class TwitterAuth extends AOAuthAuth {
    String getType() {
        "twitter"
    }

    public Map authCallback(String verifierStr, Object session) {
        authCallbackHelper(verifierStr, session) {Token accessToken ->
            def screen_name = OAuthHelper.callJsonApi(
                    config.oauth,
                    "http://api.twitter.com/1/account/verify_credentials.json",
                    accessToken.token, accessToken.secret)?.screen_name

            if (!screen_name) return null

            [
                    screen: screen_name
            ]
        }
    }
}
