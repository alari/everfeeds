package everfeeds

class AuthTagLib {
    def grailsApplication
    def evernoteAuthService
    def googleAuthService

    def enLogin = {attrs, body ->
        attrs.url = grailsApplication.config.evernote.authUrl + "?oauth_token=" + evernoteAuthService.getRequestToken()
        out << link(attrs, body())
    }

    def gLogin = {
        attrs, body ->
        attrs.url = grailsApplication.config.google.authUrl + "?oauth_token=" + googleAuthService.getRequestToken()
        out << link(attrs, body())
    }
}
