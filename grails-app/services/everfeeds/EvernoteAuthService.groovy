package everfeeds

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.springframework.web.context.request.RequestContextHolder

class EvernoteAuthService {

    static transactional = true

    def grailsApplication

    def authService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl() {
        session.evernote = new OAuthSession(grailsApplication.config.evernote)
        session.evernote.provider.setRequestHeader("User-Agent", grailsApplication.config.evernote.userAgent)
        session.evernote.getAuthUrl(controller: "access", action: "evernoteCallback")
    }

    Access processCallback(String verifier) {
        if (!session.evernote) {
            log.error "no session.evernote"
            return null
        }

        session.evernote.verify(verifier)
        String accessToken = session.evernote.token;
        session.evernote = null

        // Getting UserStore
        THttpClient userStoreTrans = new THttpClient("https://" + grailsApplication.config.evernote.host + "/edam/user");
        userStoreTrans.setCustomHeader("User-Agent", grailsApplication.config.evernote.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

        // Finding access by username
        User user = userStore.getUser(accessToken)

        authService.getAccess(
                type: Access.TYPE_EVERNOTE,
                screen: user.username,
                token: accessToken,
                shard: user.shardId
        )
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_EVERNOTE
    }
}
