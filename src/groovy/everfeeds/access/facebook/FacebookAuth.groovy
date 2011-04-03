package everfeeds.access.facebook

import everfeeds.OAuthHelper
import org.scribe.model.Token
import everfeeds.access.AOAuthAuth

/**
 * Created by alari @ 02.04.11 13:15
 */
class FacebookAuth extends AOAuthAuth {
    public Map authCallback(String verifierStr, Object session) {
        authCallbackHelper(verifierStr, session) {Token accessToken ->
            def screen_name = OAuthHelper.callJsonApi(
                    config.oauth,
                    "https://graph.facebook.com/me",
                    accessToken.token, accessToken.secret)?.id

            if (!screen_name) return null

            [
                    screen: screen_name
            ]
        }
    }
}
