package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * @author Boris G. Tsirkin
 * @author Dmitry Kurinskiy
 * @since 20.04.2011
 */
class VkontakteAuth extends OAuthAuth {
  static final String GETUSERINFO_URL = "https://api.vkontakte.ru/method/getUserInfo"
  static final String GETPROFILES_URL = "https://api.vkontakte.ru/method/getProfiles"

  VkontakteAuth() {
    key "2300127"
    secret "eejmLrWGXI7xEYifiU2D"
    provider org.scribe.builder.api.VkontakteApi
    scope "friends,wall,offline"
    type Type.VKONTAKTE
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    def authInfo = callApiJson(GETUSERINFO_URL, accessToken)
    authInfo = callApiJson(GETPROFILES_URL + "?uids=" + authInfo?.response?.user_id, accessToken)
    if (!authInfo?.response || !authInfo.response.size()) {
      return null
    }
    authInfo = authInfo.response[0]

    new AccessInfo(identity: authInfo.uid, title: authInfo.first_name + " " + authInfo.last_name)
  }
}
