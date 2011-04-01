package everfeeds

import org.scribe.model.Token

class TwitterAuthService {

    static transactional = true

    def grailsApplication

    def authService

    String getAuthUrl() {
        authService.getAuthUrl Access.TYPE_TWITTER
    }

    Access processCallback(String verifierStr) {
        authService.processCallback(Access.TYPE_TWITTER, verifierStr) { Token accessToken ->
            def screen_name = authService.oAuthCallJson(
                    "http://api.twitter.com/1/account/verify_credentials.json",
                    grailsApplication.config.twitter,
                    accessToken.token, accessToken.secret)?.screen_name

            if (!screen_name) return null

            [
                    screen: screen_name
            ]
        }
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_TWITTER
    }
}
