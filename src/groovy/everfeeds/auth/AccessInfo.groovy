package everfeeds.auth

import everfeeds.thrift.util.Type

/**
 * @author Dmitry Kurinskiy
 * @since 26.06.11 16:09
 */
class AccessInfo {
  String identity
  String accessToken
  String accessSecret
  Map<String, String> params = [:]
  String title
  String screenName
  Type type
}
