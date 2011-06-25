package everfeeds

import everfeeds.thrift.EverfeedsAPI
import everfeeds.internal.thrift.InternalAPI
import everfeeds.thrift.util.APIHolder
import everfeeds.internal.thrift.InternalAPIHolder
import everfeeds.internal.thrift.Application

class ApiService {

  static transactional = true

  private EverfeedsAPI.Client api
  private InternalAPI.Client kernelApi
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
}
