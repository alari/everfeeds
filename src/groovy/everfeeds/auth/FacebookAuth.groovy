package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * Created by alari @ 02.04.11 13:15
 */
class FacebookAuth extends OAuthAuth {
  FacebookAuth() {
    key "118265721567840"
    secret "9d43b1e1ce985e1b3f81d44e51e8cd0f"
    provider org.scribe.builder.api.FacebookApi
    scope "publish_stream,offline_access,read_stream,read_mailbox,read_insights"
    type Type.FACEBOOK
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    final authInfo = callApiJson("https://graph.facebook.com/me", accessToken)

    if (!authInfo?.id) return null

    new AccessInfo(identity: authInfo.id, title: authInfo.name ?: authInfo.username)
  }
}
