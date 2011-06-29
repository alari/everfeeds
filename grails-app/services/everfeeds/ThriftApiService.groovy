package everfeeds

import everfeeds.thrift.EverfeedsAPI
import everfeeds.internal.thrift.InternalAPI
import everfeeds.thrift.util.APIHolder
import everfeeds.internal.thrift.InternalAPIHolder
import everfeeds.internal.thrift.Application

import everfeeds.thrift.domain.Access
import everfeeds.thrift.domain.Account
import everfeeds.thrift.util.Scope
import everfeeds.thrift.domain.Token

class ThriftApiService {

  static transactional = true

  private Application app

  EverfeedsAPI.Client getApi() {
    APIHolder.client
  }

  InternalAPI.Client getInternalApi(){
    InternalAPIHolder.client
  }

  void setApp(Application app) {
    if(!this.app) this.app = app
  }

  Application getApp() {
    app
  }

  Account authenticate(AccessInfo info, String token){
    Access access = new Access()
    access.identity = info.identity
    access.type = info.type.toThrift()
    access.title = info.title
    access.screenName = info.screenName

    if(token) {
      access.accountId = api.getAccount(token).id
    }

    internalApi.authenticate(access, info.accessToken, info.accessSecret, info.params)
  }

  everfeeds.Account createToken(everfeeds.Account account) {
    Token tkn = internalApi.createToken(app.id, account.username, Scope.values()*.toString())
    account.token = tkn.key
    account.tokenExpires = new Date(tkn.expires)
    account
  }
}
