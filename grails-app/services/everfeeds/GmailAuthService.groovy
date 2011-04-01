package everfeeds

import org.scribe.model.Token
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService
import org.springframework.web.context.request.RequestContextHolder

class GmailAuthService {

    static transactional = true

    def grailsApplication

    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl() {
        OAuthService service = authService.getOAuthService(grailsApplication.config.gmail, "gmailCallback")

        Token requestToken = service.getRequestToken();
        session.gmail = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }

    Access processCallback(String verifierStr) {
        Verifier verifier = new Verifier(verifierStr);

        if (!session.gmail) {
            log.error "no session.gmail"
            return null
        }

        Token accessToken = session.gmail.service.getAccessToken(session.gmail.token, verifier);

        String token = accessToken.token
        String secret = accessToken.secret

        def email = authService.oAuthCall(
                grailsApplication.config.gmail.emailUrl,
                grailsApplication.config.gmail,
                token, secret).body

        if (email) {
            email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
        } else {
            return null
        }

        if (!email) return null

        session.gmail = null

        authService.getAccess(Access.TYPE_GMAIL, email, token, secret)
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_GMAIL
    }
}
