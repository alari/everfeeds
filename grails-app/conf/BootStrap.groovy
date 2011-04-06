import everfeeds.Package
class BootStrap {

    def init = { servletContext ->
      Package.getClasses("everfeeds.bootstrap").each{
        it.run()
      }
    }
    def destroy = {
    }
}
