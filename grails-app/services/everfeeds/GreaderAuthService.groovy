package everfeeds


import org.springframework.web.context.request.RequestContextHolder

class GreaderAuthService {
    def grailsApplication
    def springSecurityService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    String getAuthUrl() {
        session.greader = new OAuthSession(grailsApplication.config.greader)
        session.greader.getAuthUrl(controller:"access", action:"greaderCallback")
    }

    Map processCallback(String verifier) {
        if (!session.greader) return null

        session.greader.verify(verifier)

        String accessToken = session.greader.token;
        String tokenSecret = session.greader.secret

        session.greader = null

        [token: accessToken, secret: tokenSecret]
    }
}
