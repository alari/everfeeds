package everfeeds

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.springframework.web.context.request.RequestContextHolder
import org.scribe.model.Token
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService

class EvernoteAuthService {

    static transactional = true

    def grailsApplication

    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl() {
        String authUrl
        /*
        session.evernote = new OAuthSession(grailsApplication.config.evernote)
        session.evernote.provider.setRequestHeader("User-Agent", grailsApplication.config.evernote.userAgent)
        return session.evernote.getAuthUrl(controller: "access", action: "evernoteCallback")
*/

        OAuthService service = authService.getOAuthService(grailsApplication.config.evernote, "evernoteCallback")

        Token requestToken = service.getRequestToken();
        session.evernote = [service: service, token: requestToken]

        authUrl = service.getAuthorizationUrl( requestToken )
        log.debug authUrl
        authUrl
    }

    Access processCallback(String verifierStr) {

        if (!session.evernote) {
            log.error "no session.evernote"
            return null
        }
          /*
        session.evernote.verify(verifierStr)
        String token = session.evernote.token;
        */

        Verifier verifier = new Verifier(verifierStr);
        Token accessToken = session.evernote.service.getAccessToken(session.evernote.token, verifier);
        String token = accessToken.token


        session.evernote = null

        // Getting UserStore
        THttpClient userStoreTrans = new THttpClient("https://" + grailsApplication.config.evernote.host + "/edam/user");
        userStoreTrans.setCustomHeader("User-Agent", grailsApplication.config.evernote.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

        // Finding access by username
        User user = userStore.getUser(token)
        if(!user) return null

        authService.getAccess(
                type: Access.TYPE_EVERNOTE,
                screen: user.username,
                token: token,
                shard: user.shardId
        )
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_EVERNOTE
    }
}
