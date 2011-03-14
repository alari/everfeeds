package everfeeds.manager

import everfeeds.Access
import com.evernote.edam.notestore.NoteStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import com.evernote.edam.userstore.UserStore
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import com.evernote.edam.type.User
import com.evernote.edam.type.Notebook

/**
 * Created by alari @ 14.03.11 14:55
 */
class EvernoteAccess extends AAccess {

    private config = AH.application.mainContext.grailsApplication.config.evernote

    private final String userStoreUrl = "https://" + config.host + "/edam/user"
    private String noteStoreUrl

    static private UserStore.Client userStoreClient
    private User enUser
    private NoteStore.Client noteStoreClient

    EvernoteAccess(Access access) {
        this.access = access
        noteStoreUrl = "http://" + config.host + "/edam/note/" + access.shard
    }

    List<CategoryEnvelop> getCategories(){
        List<CategoryEnvelop> categories = []

        try {
            noteStore.listNotebooks(access.token).each{
                categories.add new CategoryEnvelop(identity: it.guid, title: it.name, original: it)
            }
        } catch(e){
            access.expired = true
            access.save(flush: true)
        }
        categories
    }

    List<TagEnvelop> getTags(){
        List<TagEnvelop> tags = []

        try {
            noteStore.listTags(access.token).each{
                tags.add new TagEnvelop(identity: it.guid, title: it.name, original: it)
            }
        } catch(e){
            access.expired = true
            access.save(flush: true)
        }

        tags
    }

    boolean isPullable(){
        true
    }

    boolean isPushable(){
        true
    }

    def pull(){

    }

    def push(){

    }

    protected NoteStore.Client getNoteStore() {
        if(noteStoreClient) return noteStoreClient
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        noteStoreClient = new NoteStore.Client(noteStoreProt, noteStoreProt);
        noteStoreClient
    }

    protected UserStore.Client getUserStore() {
        if(userStoreClient) return userStoreClient
        THttpClient userStoreTrans = new THttpClient(userStoreUrl);
        userStoreTrans.setCustomHeader("User-Agent", config.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        userStoreClient = new UserStore.Client(userStoreProt, userStoreProt);
        userStoreClient
    }

    User getUser() {
        if(!enUser) enUser = getUserStore().getUser(access.token)
        enUser
    }
}
