package everfeeds

import com.evernote.edam.notestore.NoteStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import com.evernote.edam.userstore.UserStore
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import com.evernote.edam.type.User

/**
 * Created by alari @ 11.03.11 16:05
 */
class Evernote {
    String token
    String shard

    def grailsApplication = AH.application.mainContext.grailsApplication

    final String userStoreUrl = "https://" + grailsApplication.config.evernote.host + "/edam/user"

    private UserStore.Client userStore
    private User enUser

    Evernote(Account account) {
        token = account.evernoteToken
        shard = account.evernoteShard
    }

    Evernote(String accessToken, String shard) {
        this.token = accessToken
        this.shard = shard
    }

    def getSession(){
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getNoteStoreUrl() {
        "http://" + grailsApplication.config.evernote.host + "/edam/note/" + shard;
    }

    NoteStore.Client getNoteStore() {
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        new NoteStore.Client(noteStoreProt, noteStoreProt);
    }

    UserStore.Client getUserStore() {
        if(userStore) return userStore
        THttpClient userStoreTrans = new THttpClient(userStoreUrl);
        userStoreTrans.setCustomHeader("User-Agent", grailsApplication.config.evernote.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        userStore = new UserStore.Client(userStoreProt, userStoreProt);
        userStore
    }

    User getUser() {
        if(!enUser) enUser = getUserStore().getUser(token)
        enUser
    }
}
