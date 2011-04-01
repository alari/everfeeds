package everfeeds

import org.scribe.model.Token
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService
import org.springframework.web.context.request.RequestContextHolder

class TwitterAuthService {

    static transactional = true

    def grailsApplication

    def springSecurityService

    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl() {
        OAuthService service = authService.getOAuthService(grailsApplication.config.twitter, "twitterCallback")

        Token requestToken = service.getRequestToken();
        session.twitter = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }

    Access processCallback(String verifierStr) {
        Verifier verifier = new Verifier(verifierStr);

        if (!session.twitter) {
            log.error "no session.twitter"
            return null
        }

        Token accessToken = session.twitter.service.getAccessToken(session.twitter.token, verifier);

        String token = accessToken.token
        String secret = accessToken.secret

        def screen_name = authService.oAuthCallJson(
                "http://api.twitter.com/1/account/verify_credentials.json",
                grailsApplication.config.twitter,
                token, secret)?.screen_name

        if (!screen_name) return null

        session.twitter = null

        authService.getAccess(Access.TYPE_TWITTER, screen_name, token, secret)
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_TWITTER
    }
}
