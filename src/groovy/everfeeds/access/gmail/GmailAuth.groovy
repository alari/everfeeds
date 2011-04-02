package everfeeds.access.gmail

import everfeeds.access.google.GoogleAuth

/**
 * Created by alari @ 02.04.11 13:15
 */
class GmailAuth extends GoogleAuth {
    static String getType() {
        "gmail"
    }

    static public Map authCallback(String verifierStr, Object session) {
        _authCallback(verifierStr, session, type, super.&authCallback)
    }
}
