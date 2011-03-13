package everfeeds

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder as SCH

class AccessController {

    def evernoteAuthService
    def greaderAuthService
    def springSecurityService

    def index = { }

    def greader = {
        redirect url: greaderAuthService.getAuthUrl()
    }

    def greaderCallback = {
        processCallback(greaderAuthService, params.oauth_verifier)
    }

    def evernote = {
        redirect url: evernoteAuthService.getAuthUrl()
    }

    def evernoteCallback = {
        processCallback(evernoteAuthService, params.oauth_verifier)
    }

    private processCallback(service, verifier) {
        Access access = service.processCallback(verifier)
        if(!access) {
            flash.message = "Access denied"
            redirect controller: "root"
        }

        setLoggedAccountAccess(access)
        service.setAccountRole access.account

        flash.message = "You're logged in"
        redirect controller: "root"
    }

    private Account createAccessAccount(Access access) {
        new Account(
                username: access.identity,
                password: springSecurityService.encodePassword(access.identity+new Random().nextInt().toString()),
                enabled: true,
	            accountExpired: false,
	            accountLocked: false,
	            passwordExpired: false
        ).save(flush: true)
    }

    private void setLoggedAccountAccess(Access access){
        // user is already logged, connect access to current account
        if(loggedIn) {
            authenticatedUser.addToAccesses access
            access.account = authenticatedUser
            access.save(flush: true)
        } else {
            if(!access.account) {
                access.account = createAccessAccount(access)
                access.account.addToAccesses access
                access.save(flush: true)
            }
            // set as logged
            SCH.context.authentication = new PreAuthenticatedAuthenticationToken(access.account, access.account)
        }
    }
}
