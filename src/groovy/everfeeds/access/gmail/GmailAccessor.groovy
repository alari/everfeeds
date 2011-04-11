package everfeeds.access.gmail

import everfeeds.Access
import everfeeds.access.Accessor
import everfeeds.envelops.EntryFace
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop
import everfeeds.OAuthHelper

/**
 * Created by alari @ 14.03.11 14:55
 */
class GmailAccessor extends Accessor {

    private static final String _FEED_URL = "https://mail.google.com/mail/feed/atom/";

    GmailAccessor(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories() {
        List<CategoryEnvelop> categories = []
        categories.add new CategoryEnvelop(identity: "inbox-unread", title: "gmail.category.inbox", titleIsCode: true)
        categories
    }

    List<TagEnvelop> getTags() {
        List<TagEnvelop> tags = []
        tags
    }

    boolean isPullable() {
        true
    }

    boolean isPushable() {
        false
    }

    public List<EntryEnvelop> pull(Map params = [:]) {
        List<EntryEnvelop> entries = []
        EntryFace entry

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

    void push(EntryFace entry) {
        void
    }

    protected apiGet(String url) {
        String result = null
        try {
            result = OAuthHelper.callApi(config.oauth, url, access.token, access.secret)
        } catch(e){}

        if (!result) {
            access.expired = true
            access.save()
            return ""
        }
        result
    }
}
