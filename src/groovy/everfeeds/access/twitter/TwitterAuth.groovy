package everfeeds.access.twitter

import everfeeds.access.AOAuthAuth
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 13:15
 */
class TwitterAuth extends AOAuthAuth {
    static String getType() {
        "twitter"
    }

    static public Map authCallback(String verifierStr, Object session) {
        authCallback(verifierStr, session, type) {Token accessToken ->
            def screen_name = callJsonApi(
                    ConfigurationHolder.config.twitter.oauth,
                    "http://api.twitter.com/1/account/verify_credentials.json",
                    accessToken.token, accessToken.secret)?.screen_name

            if (!screen_name) return null

            [
                    screen: screen_name
            ]
        }
    }
}
