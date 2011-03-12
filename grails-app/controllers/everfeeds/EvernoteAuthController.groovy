package everfeeds

import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class EvernoteAuthController {

    def evernoteAuthService
    def springSecurityService

    def callback = {
        Evernote evernote = evernoteAuthService.processOauthCallback(params.oauth_verifier)

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
