package everfeeds

import com.evernote.oauth.consumer.SimpleOAuthRequest
import org.springframework.web.context.request.RequestContextHolder

class GoogleAuthService {

    static transactional = true

    def grailsApplication
    def springSecurityService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    String getRequestToken() {
        if (session?.googleRequest) {
            return session.googleRequest
        }

        // Send an OAuth message to the Provider asking for a new Request
        // Token because we don't have access to the current user's account.
        SimpleOAuthRequest oauthRequestor =
        new SimpleOAuthRequest(grailsApplication.config.google.requestTokenUrl,
                grailsApplication.config.google.consumer.key,
                grailsApplication.config.google.consumer.secret, null);

        oauthRequestor.setParameter("oauth_callback",
                g.createLink(controller:"googleAuth", action: "callback", absolute: true) as String)
        //oauthRequestor.setParameter("scope", grailsApplication.config.google.scope)

        Map<String, String> reply = oauthRequestor.sendRequest();

        session.googleRequest = reply.get("oauth_token");
        return session.googleRequest
    }

    String processOauthCallback(verifier) {
        // Send an OAuth message to the Provider asking to exchange the
        // existing Request Token for an Access Token
        SimpleOAuthRequest oauthRequestor =
        new SimpleOAuthRequest(grailsApplication.config.google.accessTokenUrl,
                grailsApplication.config.google.consumer.key,
                grailsApplication.config.google.consumer.secret, null);

        oauthRequestor.setParameter("oauth_token", getRequestToken());
        oauthRequestor.setParameter("oauth_verifier", verifier);

        Map<String, String> reply = oauthRequestor.sendRequest();

        session.googleRequest = null

        reply.get("oauth_token")
    }

    Account createAccount(Evernote evernote) {

        Account account = new Account(
                username: "en:" + evernote.user.username,
                password: springSecurityService.encodePassword(evernote.user.username + new Random().nextInt().toString()),

                evernoteUser: evernote.user.username,
                evernoteToken: evernote.token,
                evernoteShard: evernote.shard,

                enabled: true,
                passwordExpired: false,
                accountExpired: false,
                accountLocked: false
        ).save()

        Role role = Role.findByAuthority("EVERNOTE") ?: new Role(authority: "EVERNOTE").save()
        AccountRole.create account, role

        account.save(flush: true)
    }
}
