package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * Created by alari @ 02.04.11 13:15
 */
class TwitterAuth extends OAuthAuth {
  static final String CREDENTIALS_URL = "http://api.twitter.com/1/account/verify_credentials.json"

  TwitterAuth() {
    key "A5maG2S6WHvloLeFDeIw"
    secret "2QFVqw7L0GISHTgdB11GHcmhJo970qRmt2Tg10"
    provider org.scribe.builder.api.TwitterApi
    type Type.TWITTER
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    final authInfo = callApiJson(CREDENTIALS_URL, accessToken)

    authInfo?.id ? new AccessInfo(identity: authInfo?.id, title: authInfo.screen_name, screenName: authInfo.screen_name) : null
  }
}
