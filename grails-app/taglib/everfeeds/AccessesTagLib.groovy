package everfeeds

class AccessesTagLib {
    def showAccesses = {
        String inner
        Access.MANAGERS.keySet().each {
            inner = "<img src=\"${resource(dir:'images/social',file:it+'.jpg')}\" width='40' height='40' alt='${it}'/> ${it}"
            out << "<p>"+link(controller:"access", action:it, inner)+"</p>"
        }
    }
}
