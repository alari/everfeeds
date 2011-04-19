package everfeeds.access.facebook

import everfeeds.OAuthHelper
import everfeeds.access.OAuthAuth
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 13:15
 */
class FacebookAuth extends OAuthAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session) {Token accessToken ->
      final authInfo = OAuthHelper.callJsonApi(
          config.oauth,
          "https://graph.facebook.com/me",
          accessToken.token, accessToken.secret)


      if (!authInfo) return null

      [
          id: authInfo.id,
          title: authInfo.name ?: authInfo.username
      ]
    }
  }
}
