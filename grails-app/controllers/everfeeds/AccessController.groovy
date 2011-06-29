package everfeeds

import org.springframework.security.core.context.SecurityContextHolder as SCH

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class AccessController {

  def syncService
  def authService
  def springSecurityService
  def thriftApiService
  def i18n

  def index = {
    flash.error = i18n."access.error.index"
    redirect controller: "root", action: "index"
  }

  def auth = {
    log.debug "AUTH for ${params.id}"
    String authUrl = authService.getAuthUrl(params.id, session)
    if(authUrl) {
      redirect url: authUrl
    } else {
      flash.error = i18n."access.error.unknownprovider"([params.id])
      redirect controller: "root", action: "index"
    }
  }

  def callback = {
    log.debug "CALLBACK for ${params.id}"

    AccessInfo accessInfo = authService.processCallback(params.id, params.oauth_verifier ?: params.code, session)
    if(!accessInfo) {
      flash.error = i18n."access.error.denied"([params.id])
      redirect controller: "root", action: "index"
      return
    }

    Account account = getAccountByThrift( thriftApiService.authenticate(accessInfo, springSecurityService.currentUser?.token) )
    // if token is going to expire, renew
    if(!account.token || account.tokenExpires>new Date()-5) {
      thriftApiService.createToken(account)
    }
    // set account as logged in
    SCH.context.authentication = new PreAuthenticatedAuthenticationToken(account, account, account.authorities)

    flash.message = i18n."access.loggedin"([i18n."${params.id}.title"])
    redirect controller: "root", action: "index"
  }

  private Account getAccountByThrift(everfeeds.thrift.domain.Account account) {  log.debug(account.id+"|"+account.title)
    Account a = Account.findByUsername(account.id)
    if(a) return a

    a = new Account(
       title: account.title ?: "Account",
        username: account.id,
        password: springSecurityService.encodePassword(account.id + new Random().nextInt().toString()),
        enabled: true,
        accountExpired: false,
        accountLocked: false,
        passwordExpired: false
    )
    if(!a.validate()) {
      log.error a.errors
    }
    a.save(flush: true).addAuthority("ROLE_ACCOUNT", true)
  }
}
