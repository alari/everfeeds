package everfeeds

class AccessController {

    def evernoteAuthService
    def greaderAuthService

    def index = { }

    def greader = {
        redirect url: greaderAuthService.getAuthUrl()
    }

    def greaderCallback = {
        def credentials = greaderAuthService.processCallback(params.oauth_verifier)

        render credentials
    }

    def evernote = {
        redirect url: evernoteAuthService.getAuthUrl()
    }

    def evernoteCallback = {
        def credentials = evernoteAuthService.processCallback(params.oauth_verifier)

        render credentials as String
        render "<br/>"
        render "${credentials.identity} ${credentials.shard}"
        render "<hr/>"
        render "<a href='/everfeeds/'>back</a>"
    }
}
