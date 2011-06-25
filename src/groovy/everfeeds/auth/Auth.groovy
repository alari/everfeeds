package everfeeds.auth

import org.apache.log4j.Logger
import everfeeds.Manager

/**
 * Created by alari @ 02.04.11 12:57
 */
abstract class Auth {
  abstract public String getAuthUrl(def session)

  private String typeCache

  protected getLog() {
    Logger.getLogger(this.class)
  }

  public String getType() {
    if (!typeCache) {
      typeCache = this.class.package.name.tokenize(".").last()
    }
    typeCache
  }

  public Map getConfig() {
    Manager.getConfig(type)
  }
}
