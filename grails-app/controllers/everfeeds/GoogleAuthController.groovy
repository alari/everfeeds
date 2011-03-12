package everfeeds

import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class GoogleAuthController {

    def googleAuthService
    def springSecurityService

    def callback = {
       String token = googleAuthService.processOauthCallback(params.oauth_verifier)
        log.error "OKAY: "+token
        render token
        return

        Account account = Account.findByEvernoteUser(evernote.user.username)

        if(account) {
            // update access token
            account.evernoteToken = evernote.token
            account.save()
        } else {
            // create a new account
            account = evernoteAuthService.createAccount(evernote)
        }

        // set as logged
        SCH.context.authentication = new PreAuthenticatedAuthenticationToken(account, account)

        redirect controller: "root"
    }
}
