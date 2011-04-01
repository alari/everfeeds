package everfeeds

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder as SCH

class AccessController {

    def evernoteAuthService
    def greaderAuthService
    def twitterAuthService
    def springSecurityService
    def syncService
    def gmailAuthService

    def index = {
        flash.message = "Error: somehow we got to Access::index"
        redirect controller: "root"
    }

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

    def twitter = {
        redirect url: twitterAuthService.getAuthUrl()
    }

    def twitterCallback = {
        processCallback(twitterAuthService, params.oauth_verifier)
    }

    def gmail = {
        redirect url: gmailAuthService.getAuthUrl()
    }

    def gmailCallback = {
        processCallback(gmailAuthService, params.oauth_verifier)
    }

    private processCallback(service, verifier) {
        Access access = service.processCallback(verifier)
        if(!access) {
            flash.message = "Access denied for ${verifier}"
            redirect controller: "root"
            return
        }

        setLoggedAccountAccess(access)
        service.setAccountRole access.account
        access.expired = false
        access.save()

        flash.message = "You're logged in"
        redirect controller: "root"
    }

    private Account createAccessAccount(Access access) {
        Account account = new Account(
                username: access.identity,
                password: springSecurityService.encodePassword(access.identity+new Random().nextInt().toString()),
                enabled: true,
	            accountExpired: false,
	            accountLocked: false,
	            passwordExpired: false
        ).save(flush: true)
        AccountRole.create(account, Role.getByAuthority("ROLE_ACCOUNT"), true)
        log.debug "Created access account: ${account} with "+account.authorities*.authority.join(";")
        account
    }

    private void setLoggedAccountAccess(Access access){
        // user is already logged, connect access to current account
        if(loggedIn) {
            authenticatedUser.addToAccesses access
            access.account = authenticatedUser
            access.save(flush: true)
            syncService.addToQueue access, true
            // Adding to a MAX queue
        } else {
            if(!access.account) {
                access.account = createAccessAccount(access)
                access.account.addToAccesses access
                access.save(flush: true)
                // Adding to a MAX queue
            } else {
                // Adding to a CUURENT queue
            }
            // set as logged
            log.debug "Setting as logged with authorities: "+access.account.authorities*.authority.join(";")
            SCH.context.authentication = new PreAuthenticatedAuthenticationToken(access.account, access.account, access.account.authorities)
            syncService.addToQueue access.account, true
        }
    }
}
