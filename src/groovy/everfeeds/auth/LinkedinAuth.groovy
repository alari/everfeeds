package everfeeds.auth

import org.scribe.model.Token
import everfeeds.thrift.util.Type

/**
 * Created by alari @ 02.04.11 13:15
 */
class LinkedInAuth extends OAuthAuth {
  static final String CREDENTIALS_URL = "http://api.linkedin.com/v1/people/~:(id)"

  LinkedInAuth() {
    key "LYuiN2KtQJVJcHOggAZsMT20HzezFqMFvHlAVsaUju5y7gjhc6Y3BJpFLg86QNBX"
    secret "p93hwB8RY5g7BaEwXc4qZbZhZ7Zqsxl0Rv4TRV9zcW4dBeNuTCfFv5rwjyACUL1U"
    provider org.scribe.builder.api.LinkedInApi
    type Type.LINKEDIN
  }

  protected AccessInfo getAccessInfo(Token accessToken) {
    final authInfo = callApiXml(CREDENTIALS_URL, accessToken)

    authInfo?.id ? new AccessInfo(identity: authInfo.id) : null
  }
}
