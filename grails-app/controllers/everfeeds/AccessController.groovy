package everfeeds

import org.springframework.security.core.context.SecurityContextHolder as SCH

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import everfeeds.access.Manager

class AccessController {

    def syncService
    def authService
    def springSecurityService
    def i18n

    def index = {
        flash.error = i18n."access.error.index"
        redirect controller: "root", action: "index"
    }

    def auth = {
        log.debug "AUTH for ${params.id}"
        if (!ConfigurationHolder.config.access."${params.id}"?.auth instanceof Class) {
            flash.error = i18n."access.error.unknownprovider"([params.id])
            redirect controller: "root", action: "index"
        } else {
            redirect url: authService.getAuthUrl(params.id)
        }
    }

    def callback = {
        log.debug "CALLBACK for ${params.id}"
        if (!Manager.getAuth(params.id)) {
            log.error "Unknown auth provider: ${params.id}"
            flash.error = i18n."access.error.unknownprovider"([params.id])
            redirect controller: "root", action: "index"
            return
        }

        Access access = null
        try {
            access = authService.processCallback(params.id, params.oauth_verifier ?: params.code)
        } catch(ignore){
            log.error "Access denied", ignore
        }

        if (!access) {
            flash.error = i18n."access.error.denied"([params.id])
            redirect controller: "root", action: "index"
            return
        }

        setLoggedAccountAccess(access)
        authService.setAccountRole(access.account, params.id)
        access.expired = false
        access.save()

        flash.message = i18n."access.loggedin"([i18n."${params.id}.title"])
        redirect controller: "root", action: "index"
    }

    private Account createAccessAccount(Access access) {
        Account account = new Account(
                username: access.identity,
                password: springSecurityService.encodePassword(access.identity + new Random().nextInt().toString()),
                enabled: true,
                accountExpired: false,
                accountLocked: false,
                passwordExpired: false
        ).save(flush: true)
        AccountRole.create(account, Role.getByAuthority("ROLE_ACCOUNT"), true)
        log.debug "Created access account: ${account} with " + account.authorities*.authority.join(";")
        account
    }

    private void setLoggedAccountAccess(Access access) {
        // user is already logged, connect access to current account
        if (loggedIn) {
            authenticatedUser.addToAccesses access
            access.account = authenticatedUser
            access.save(flush: true)
            syncService.addToQueue access, [pull: true, num: Manager.MAX_NUM]
            // Adding to a MAX queue
        } else {
            if (!access.account) {
                access.account = createAccessAccount(access)
                access.account.addToAccesses access
                access.save(flush: true)
                // Adding to a MAX queue
                syncService.addToQueue access, [pull: true, num: Manager.MAX_NUM]
            } else {
                // Adding to a CURRENT queue
                syncService.addToQueue access, [pull: true]
            }
            // set as logged
            log.debug "Setting as logged with authorities: " + access.account.authorities*.authority.join(";")
            SCH.context.authentication = new PreAuthenticatedAuthenticationToken(access.account, access.account, access.account.authorities)
        }
    }
}
