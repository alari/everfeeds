package everfeeds

import everfeeds.access.Manager

class AccessesTagLib {
    def i18n

    def showAccesses = {
        String inner
        Manager.getConfigs().each{type,params->
            if(params?.auth == false) {
              return
            }
          inner = i18n."${type}.title"(null, "html")
            inner = "<img src=\"${resource(dir:'images/social',file:type+'.jpg')}\" width='40' height='40' alt='${inner}'/> ${inner}"
            out << "<p>"+link(controller:"access", action:type, inner)+"</p>"
        }
    }
}
