package everfeeds.access.google

import everfeeds.OAuthHelper
import everfeeds.access.OAuthAuth
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 12:59
 */
abstract class GoogleAuth extends OAuthAuth {
  protected callbackClosure = {Token accessToken ->
    String email = OAuthHelper.callApi(
        config.oauth,
        config.emailUrl,
        accessToken.token, accessToken.secret
    )

    if (email) {
      email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
    } else {
      return null
    }

    if (!email) return null

    [
        id: email
    ]
  }
}
