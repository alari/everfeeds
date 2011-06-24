package everfeeds

import everfeeds.thrift.EverfeedsAPI
import everfeeds.internal.thrift.InternalAPI
import everfeeds.thrift.util.APIHolder
import everfeeds.internal.thrift.InternalAPIHolder

class ApiService {

  static transactional = true

  private EverfeedsAPI.Client api
  private InternalAPI.Client kernelApi

  EverfeedsAPI.Client getApi() {
    APIHolder.client
  }

  InternalAPI.Client getInternalApi(){
    InternalAPIHolder.client
  }
}
