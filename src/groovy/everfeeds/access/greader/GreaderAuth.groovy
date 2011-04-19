package everfeeds.access.greader

import everfeeds.access.google.GoogleAuth

/**
 * Created by alari @ 02.04.11 13:14
 */
class GreaderAuth extends GoogleAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session, callbackClosure)
  }
}
