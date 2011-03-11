package everfeeds

class EvernoteTagLib {
    def grailsApplication
    def evernoteAuthService

    def enLogin = {attrs, body ->
        attrs.url = grailsApplication.config.evernote.authUrl + "?oauth_token=" + evernoteAuthService.getRequestToken()
        out << link(attrs, body())
    }
}
