package everfeeds.access.evernote

import com.evernote.edam.notestore.NoteFilter
import com.evernote.edam.notestore.NoteStore
import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore

import everfeeds.Access
import everfeeds.access.Accessor
import everfeeds.envelops.CategoryFace
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagFace
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop

import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import everfeeds.access.Manager

/**
 * Created by alari @ 14.03.11 14:55
 */
class EvernoteAccessor extends Accessor {
    private final String userStoreUrl = "https://" + config.host + "/edam/user"
    private String noteStoreUrl

    static private UserStore.Client userStoreClient
    private User enUser
    private NoteStore.Client noteStoreClient

    EvernoteAccessor(Access access) {
        this.access = access
        noteStoreUrl = "http://" + config.host + "/edam/note/" + access.shard
    }

    List<CategoryEnvelop> getCategories() {
        List<CategoryEnvelop> categories = []

        try {
            noteStore.listNotebooks(access.token).each {
                categories.add new CategoryEnvelop(identity: it.guid, title: it.name, original: it)
            }
        } catch (e) {
            access.expired = true
            access.save(flush: true)
        }
        categories
    }

    List<TagEnvelop> getTags() {
        List<TagEnvelop> tags = []

        try {
            noteStore.listTags(access.token).each {
                tags.add new TagEnvelop(identity: it.guid, title: it.name, original: it)
            }
        } catch (e) {
            access.expired = true
            access.save(flush: true)
        }

        tags
    }

    boolean isPullable() {
        true
    }

    boolean isPushable() {
        true
    }

    public List<EntryEnvelop> pull(Map params = [:]) {
        NoteFilter filter = new NoteFilter()
        // Categories
        if (params.category && params.category instanceof CategoryFace) {
            CategoryFace category = params.category
            filter.setNotebookGuid category.identity
        }
        // Tags
        if (params.tags && params.tags instanceof List<TagFace>) {
            List<TagFace> tags = params.tags
            filter.setTagGuids tags*.identity
        }
        // Words
        if (params.search) {
            filter.setWords((String) params.search)
        }
        // Max count
        int num = params.num ?: Manager.NUM

        List<EntryEnvelop> entries = []
        EntryFace entry

        noteStore.findNotes(access.token, filter, 0, num).notes.each {
            entry = new EntryEnvelop(
                    title: it.title,
                    content: getNoteContent(it.guid),
                    identity: it.guid,
                    author: it.attributes.author,
                    tagIdentities: it.tagGuids,
                    categoryIdentity: it.notebookGuid,
                    sourceUrl: it.attributes.sourceURL,
                    placedDate: new Date(it.created),
                    accessId: access.id
            )
            if (params?.store) {
                entry.store()
            } else {
                entries.add entry
            }
        }
        entries
    }

    void push(EntryFace entry) {

    }

    protected NoteStore.Client getNoteStore() {
        if (noteStoreClient) return noteStoreClient
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        noteStoreClient = new NoteStore.Client(noteStoreProt, noteStoreProt);
        noteStoreClient
    }

    protected UserStore.Client getUserStore() {
        if (userStoreClient) return userStoreClient
        THttpClient userStoreTrans = new THttpClient(userStoreUrl);
        userStoreTrans.setCustomHeader("User-Agent", config.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        userStoreClient = new UserStore.Client(userStoreProt, userStoreProt);
        userStoreClient
    }

    User getUser() {
        if (!enUser) enUser = getUserStore().getUser(access.token)
        enUser
    }

    private String getNoteContent(String guid) {
        Jsoup.clean(noteStore.getNoteContent(access.token, guid), Whitelist.relaxed())
    }
}
