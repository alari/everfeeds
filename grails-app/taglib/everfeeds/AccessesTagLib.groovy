package everfeeds

import everfeeds.access.Manager

class AccessesTagLib {
    def showAccesses = {
        String inner
        Manager.getConfigs().each{type,params->
            if(!params.auth) return
            inner = "<img src=\"${resource(dir:'images/social',file:type+'.jpg')}\" width='40' height='40' alt='${params.title}'/> ${params.title}"
            out << "<p>"+link(controller:"access", action:type, inner)+"</p>"
        }
    }
}
