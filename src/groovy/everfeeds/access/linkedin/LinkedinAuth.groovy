package everfeeds.access.linkedin

import everfeeds.OAuthHelper
import everfeeds.access.OAuthAuth
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 13:15
 */
class LinkedinAuth extends OAuthAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session) {Token accessToken ->
      def screen_name = OAuthHelper.callXmlApi(
          config.oauth,
          "http://api.linkedin.com/v1/people/~:(id)",
          accessToken.token, accessToken.secret)?.id

      if (!screen_name) return null

      [
          id: screen_name
      ]
    }
  }
}
