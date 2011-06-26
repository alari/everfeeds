package everfeeds.bootstrap

import everfeeds.internal.thrift.InternalAPIHolder
import everfeeds.internal.thrift.Application
import everfeeds.thrift.util.Scope
import everfeeds.SpringUtil
import everfeeds.ThriftApiService

/**
 * @author Dmitry Kurinskiy
 * @since 25.06.11 1:16
 */
class CreateAppBoot {
  static run(){
    Application app = InternalAPIHolder.client.listApps().find {it.key=="everfeeds"}
    if(!app) {
      app = new Application()
      app.key = "everfeeds"
      app.scopes = Scope.values()*.toString()
      app.title = "Everfeeds.com"
      app.description = "Main Everfeeds.com Application"
      app = InternalAPIHolder.client.saveApp(app)
    }
    ((ThriftApiService)SpringUtil.getBean("thriftApiService")).app = app
    System.out.println "Everfeeds Thrift Application ID: ${app.id}"
  }
}
