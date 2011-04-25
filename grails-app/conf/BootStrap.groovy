import everfeeds.Package

class BootStrap {

  def init = { servletContext ->
    // If you wish to add something to bootstrap, DO NOT modify this file!
    // Instead add a class with static public run() to everfeeds.bootstrap namespace.
    Package.getClasses("everfeeds.bootstrap").each {
      it.run()
    }
  }
  def destroy = {
  }
}
