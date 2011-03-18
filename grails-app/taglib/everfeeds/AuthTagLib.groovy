package everfeeds

class AuthTagLib {
    def grailsApplication
    def evernoteAuthService
    def greaderAuthService

    def auth = {attrs, body->
        out << link([controller: "access", action:attrs.type], body())
    }

}
