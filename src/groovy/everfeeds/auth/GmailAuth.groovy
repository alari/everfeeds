package everfeeds.auth

/**
 * Created by alari @ 02.04.11 13:15
 */
class GmailAuth extends GoogleAuth {
  public Map authCallback(String verifierStr, Object session) {
    authCallbackHelper(verifierStr, session, callbackClosure)
  }
}
