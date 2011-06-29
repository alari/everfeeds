package everfeeds.auth

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.scribe.model.Token
import everfeeds.thrift.util.Type
import everfeeds.AccessInfo
import everfeeds.OAuthAuth
import everfeeds.annotation.Accessor

/**
 * Created by alari @ 02.04.11 13:18
 */
@Accessor(Type.EVERNOTE)
class EvernoteAuth extends OAuthAuth {
  EvernoteAuth() {
    key "everfeeds"
    secret "dd0ba24f027198c6"
    provider org.scribe.builder.api.EvernoteApi
    type Type.EVERNOTE
  }

  static final String USER_AGENT = "everfeeds.com"
  static final String HOST = "www.evernote.com"

  protected AccessInfo getAccessInfo(Token accessToken) {
    // Getting UserStore
    THttpClient userStoreTrans = new THttpClient("https://" + HOST + "/edam/user");
    userStoreTrans.setCustomHeader("User-Agent", USER_AGENT);
    TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
    UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

    // Finding access by username
    User user = userStore.getUser(accessToken.token)
    if (!user) return null

    new AccessInfo(identity: user.username, params: [shard: user.shardId])
  }
}
