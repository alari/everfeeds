package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * Created by alari @ 02.04.11 13:14
 */
class GreaderAuth extends OAuthAuth {
  static final String EMAIL_URL = "https://www.googleapis.com/userinfo/email"

  GreaderAuth() {
    key "everfeeds.com"
    secret "mucd4gqA1yLtrY6eMzZo3IYe"
    provider org.scribe.builder.api.GoogleApi
    scope "http://www.google.com/reader/api/ http://www.google.com/reader/atom/ https://www.googleapis.com/auth/userinfo#email"
    type Type.GREADER
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    String email = callApi(EMAIL_URL, accessToken)
    email = email ? email.substring(email.indexOf("=") + 1, email.indexOf("&")) : null
    email ? new AccessInfo(identity: email) : null
  }
}
