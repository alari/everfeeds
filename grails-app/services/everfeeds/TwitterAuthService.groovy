package everfeeds

import org.scribe.oauth.OAuthService
import org.springframework.web.context.request.RequestContextHolder
import org.scribe.model.*

class TwitterAuthService {

    static transactional = true

    def grailsApplication

    def springSecurityService

    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

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

        def screen_name = authService
                .oAuthCallJson(
                    "http://api.twitter.com/1/account/verify_credentials.json",
                grailsApplication.config.twitter,
                token, secret)?.screen_name

        session.twitter = null

        Access access = Access.findByIdentity(Access.TYPE_TWITTER + ":" + screen_name) ?: new Access(
                identity: Access.TYPE_TWITTER + ":" + screen_name,
                type: Access.TYPE_TWITTER
        )

        access.token = token
        access.secret = secret
        access.expired = false
        access.save()
    }

    def setAccountRole(Account account) {
        AccountRole.create account, Role.findByAuthority("TWITTER") ?: new Role(authority: "TWITTER").save()
    }
}
