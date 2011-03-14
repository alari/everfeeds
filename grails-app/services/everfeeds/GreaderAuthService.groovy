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
        session.greader.getAuthUrl(controller: "access", action: "greaderCallback")
    }

    Access processCallback(String verifier) {
        if (!session.greader) return null

        session.greader.verify(verifier)

        String accessToken = session.greader.token;
        String tokenSecret = session.greader.secret

        String email = session.greader.apiGet(grailsApplication.config.greader.emailUrl)
        // email=fred.example@gmail.com&is_verified=true

        session.greader = null

        if (email) {
            email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
        } else {
            return null
        }

        Access access = Access.findByIdentity(Access.TYPE_GREADER + ":" + email)
        if(!access) {
            access = new Access(identity: Access.TYPE_GREADER + ":" + email, type: Access.TYPE_GREADER)
        }

        access.token = accessToken
        access.secret = tokenSecret
        access.save()
    }

    def setAccountRole(Account account) {
        AccountRole.create account, Role.findByAuthority("GREADER") ?: new Role(authority: "GREADER").save()
    }
}
