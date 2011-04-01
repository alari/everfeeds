package everfeeds

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.scribe.model.Token

class EvernoteAuthService {

    static transactional = true

    def grailsApplication

    def authService


    String getAuthUrl() {
        authService.getAuthUrl Access.TYPE_EVERNOTE
    }

    Access processCallback(String verifierStr) {
        authService.processCallback(Access.TYPE_EVERNOTE, verifierStr){ Token accessToken ->
            // Getting UserStore
            THttpClient userStoreTrans = new THttpClient("https://" + grailsApplication.config.evernote.host + "/edam/user");
            userStoreTrans.setCustomHeader("User-Agent", grailsApplication.config.evernote.userAgent);
            TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
            UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

            // Finding access by username
            User user = userStore.getUser(accessToken.token)
            if(!user) return null

            [
                    screen: user.username,
                    shard: user.shardId
            ]
        }
    }

    def setAccountRole(Account account) {
        authService.setAccountRole account, Access.TYPE_EVERNOTE
    }
}
