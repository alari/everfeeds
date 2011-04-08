package everfeeds.access.evernote

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import everfeeds.access.AOAuthAuth
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 13:18
 */
class EvernoteAuth extends AOAuthAuth {
    public Map authCallback(String verifierStr, Object session) {
        authCallbackHelper(verifierStr, session) {Token accessToken ->
            // Getting UserStore
            THttpClient userStoreTrans = new THttpClient("https://" + config.host + "/edam/user");
            userStoreTrans.setCustomHeader("User-Agent", config.userAgent);
            TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
            UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

            // Finding access by username
            User user = userStore.getUser(accessToken.token)
            if (!user) return null

            [
                    id: user.username,
                    title: user.username,
                    shard: user.shardId
            ]
        }
    }
}
