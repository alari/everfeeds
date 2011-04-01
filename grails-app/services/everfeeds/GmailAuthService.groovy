package everfeeds

import org.scribe.model.Token

class GmailAuthService {

    static transactional = true

    def grailsApplication

    def authService

    String getAuthUrl() {
        authService.getAuthUrl Access.TYPE_GMAIL
    }

    Access processCallback(String verifierStr) {
        authService.processCallback(Access.TYPE_GMAIL, verifierStr) { Token accessToken ->
            def email = authService.oAuthCall(
                    grailsApplication.config.gmail.emailUrl,
                    grailsApplication.config.gmail,
                    accessToken.token, accessToken.secret).body

            if (email) {
                email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
            } else {
                return null
            }

            if (!email) return null

            [
                    screen: email
            ]
        }
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_GMAIL
    }
}
