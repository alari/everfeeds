import everfeeds.Package
import org.apache.commons.lang.StringUtils

class BootStrap {

  def init = { servletContext ->
    String.metaClass.mixin StringUtils
    Package.getClasses("everfeeds.bootstrap").each {
      it.run()
    }
  }
  def destroy = {
  }
}
