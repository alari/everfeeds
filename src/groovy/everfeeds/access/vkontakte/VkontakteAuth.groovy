package everfeeds.access.vkontakte

import everfeeds.OAuthHelper
import everfeeds.access.OAuthAuth
import org.scribe.model.Token

/**
 * @author Boris G. Tsirkin
 * @since 20.04.2011
 */
class VkontakteAuth extends OAuthAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session) {Token accessToken ->
      OAuthHelper.callJsonApi(
        config.oauth,
        "https://api.vkontakte.ru/method/getUserSettings",
        accessToken.token,
        accessToken.secret
      )

      final authInfo = OAuthHelper.callJsonApi(
        config.oauth,
        "https://api.vkontakte.ru/method/getUserSettings",
        accessToken.token,
        accessToken.secret
      )
      if (!authInfo) {
        return null
      }

      [
        id: authInfo.id
      ]
    }
  }
}
