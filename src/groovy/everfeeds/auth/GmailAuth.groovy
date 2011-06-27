package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * Created by alari @ 02.04.11 13:15
 */
class GmailAuth extends OAuthAuth {
  static final String EMAIL_URL = "https://www.googleapis.com/userinfo/email"

  GmailAuth() {
    key "everfeeds.com"
    secret "mucd4gqA1yLtrY6eMzZo3IYe"
    provider org.scribe.builder.api.GoogleApi
    scope "https://www.googleapis.com/auth/userinfo#email https://mail.google.com/mail/feed/atom/"
    type Type.GMAIL
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    String email = callApi(EMAIL_URL, accessToken)
    email = email ? email.substring(email.indexOf("=") + 1, email.indexOf("&")) : null
    email ? new AccessInfo(identity: email) : null
  }
}
