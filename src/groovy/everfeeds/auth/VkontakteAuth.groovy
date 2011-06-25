package everfeeds.auth

import everfeeds.OAuthHelper
import everfeeds.auth.OAuthAuth
import org.scribe.model.Token

/**
 * @author Boris G. Tsirkin
 * @since 20.04.2011
 */
class VkontakteAuth extends OAuthAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session) {Token accessToken ->
      def authInfo = OAuthHelper.callJsonApi(
          config.oauth,
          "https://api.vkontakte.ru/method/getUserInfo",
          accessToken.token,
          accessToken.secret
      )

      authInfo = OAuthHelper.callJsonApi(
          config.oauth,
          "https://api.vkontakte.ru/method/getProfiles?uids=${authInfo?.response?.user_id}",
          accessToken.token,
          accessToken.secret
      )

      if (!authInfo?.response || ! authInfo.response.size()) {
        return null
      }

      authInfo = authInfo.response[0]

      [
          id: authInfo.uid,
          title: authInfo.first_name + " " + authInfo.last_name
      ]
    }
  }
}
