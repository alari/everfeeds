package everfeeds

class AuthTagLib {
    def grailsApplication
    def evernoteAuthService
    def greaderAuthService

    def enLogin = {attrs, body ->
        //attrs.url = grailsApplication.config.evernote.authUrl + "?oauth_token=" + evernoteAuthService.getRequestToken()
        out << link([controller: "access", action:"evernote"], body())
    }

    def gLogin = {
        attrs, body ->
        //attrs.url = grailsApplication.config.google.authUrl + "?oauth_token=" + googleAuthService.getRequestToken()
        out << link([controller:"access", action:"greader"], body())
        //out << link(attrs, body())
    }
}
