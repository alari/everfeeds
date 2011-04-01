package everfeeds

import org.scribe.model.Token

class GreaderAuthService {
    def grailsApplication
    def authService

    String getAuthUrl() {
        authService.getAuthUrl Access.TYPE_GREADER
    }

    Access processCallback(String verifierStr) {
        authService.processCallback(Access.TYPE_GREADER, verifierStr) { Token accessToken ->
            def email = authService.oAuthCall(
                    grailsApplication.config.greader.emailUrl,
                    grailsApplication.config.greader,
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
        authService.setAccountRole account, Access.TYPE_GREADER
    }
}
