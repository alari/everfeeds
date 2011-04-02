package everfeeds.access.gmail

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import everfeeds.Access
import everfeeds.OAuthSession
import everfeeds.access.AAccess
import everfeeds.access.IEntry
import everfeeds.access.envelops.CategoryEnvelop
import everfeeds.access.envelops.EntryEnvelop
import everfeeds.access.envelops.TagEnvelop

/**
 * Created by alari @ 14.03.11 14:55
 */
class GmailAccess extends AAccess {

    private static final String _FEED_URL = "https://mail.google.com/mail/feed/atom/";

    private config = AH.application.mainContext.grailsApplication.config.access.gmail


    GmailAccess(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories() {
        List<CategoryEnvelop> categories = []

        //apiGet(_SUBSCRIPTION_LIST_URL)?.subscriptions?.each{
        //    categories.add new CategoryEnvelop(identity: "unread inbox", title: "unread inbox")
        //}
        categories.add new CategoryEnvelop(identity: "inbox-unread", title: "Inbox Unread")

        categories
    }

    List<TagEnvelop> getTags() {
        List<TagEnvelop> tags = []
        /*
        apiGet(_TAG_LIST_URL)?.tags?.each{
            // TODO: add localized tag names
            tags.add new TagEnvelop(identity: it.id, title: it.id.substring(it.id.lastIndexOf("/")+1), original: it)
        }
          */
        tags
    }

    boolean isPullable() {
        true
    }

    boolean isPushable() {
        false
    }

    public List<EntryEnvelop> pull(Map params = [:]) {
        /*
        String url
        // Category
        if(params.category && params.category instanceof ICategory) {
            ICategory category = params.category
            url = _CONTENT_BASE_URL + category.identity
        } else {
            url = _CONTENT_READER_LIST
        }

        // TODO: handle tags; now it doesn't work

        // Max num
        int num = params.num ?: NUM

        url += "?ck="+System.currentTimeMillis()/1000
        url += "&n=" + num
                               */
        List<EntryEnvelop> entries = []
        IEntry entry

        new XmlSlurper().parseText(apiGet(_FEED_URL).toString())?.entry?.each {
            entry = new EntryEnvelop(
                    title: it.title.text(),
                    content: it.summary.text(),
                    identity: it.id,
                    author: it.author.name.text() + " &lt;" + it.author.email.text() + "&gt;",
                    categoryIdentity: "inbox-unread",
                    sourceUrl: it.link.@href,
                    placedDate: new Date(),
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

    void push(IEntry entry) {
        void
    }

    protected apiGet(String url) {
        OAuthSession s = new OAuthSession(config);
        s.consumer.setTokenWithSecret(access.token, access.secret)

        //url += (url.indexOf("?")>-1 ? "&" : "?")+"output=json"

        String result = s.apiGet(url)

        if (!result) {
            access.expired = true
            access.save()
            return [:]
        }
        result
    }
}
