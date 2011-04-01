package everfeeds

import org.springframework.web.context.request.RequestContextHolder
import org.scribe.oauth.OAuthService
import org.scribe.model.Token
import org.scribe.model.Verifier

class GreaderAuthService {
    def grailsApplication
    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl() {
        OAuthService service = authService.getOAuthService(grailsApplication.config.greader, "greaderCallback")

        Token requestToken = service.getRequestToken();
        session.greader = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }

    Access processCallback(String verifierStr) {
        Verifier verifier = new Verifier(verifierStr)

        if (!session.greader) {
            log.error "no session.greader"
            return null
        }

        Token accessToken = session.greader.service.getAccessToken(session.greader.token, verifier);

        String token = accessToken.token
        String secret = accessToken.secret

        def email = authService.oAuthCall(
                grailsApplication.config.greader.emailUrl,
                grailsApplication.config.greader,
                token, secret).body

        if (email) {
            email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
        } else {
            return null
        }

        if (!email) return null

        session.gmail = null

        authService.getAccess(Access.TYPE_GREADER, email, token, secret)
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_GREADER
    }
}
