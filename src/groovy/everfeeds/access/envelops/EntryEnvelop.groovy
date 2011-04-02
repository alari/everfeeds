package everfeeds.access.envelops

import everfeeds.Access
import everfeeds.Category
import everfeeds.Entry
import everfeeds.access.IEntry

/**
 * Created by alari @ 14.03.11 17:21
 */
class EntryEnvelop implements IEntry {
    String identity
    String title
    String imageUrl
    String content
    String author
    String sourceUrl
    Date placedDate

    List<String> tagIdentities
    String categoryIdentity

    int accessId

    Entry store() {
        Access access = Access.get(accessId)
        // Check uniqueness
        if (Entry.countByAccessAndIdentity(access, identity)) return;
        Entry entry = new Entry(
                identity: identity,
                title: title,
                imageUrl: imageUrl,
                content: content,
                author: author,
                sourceUrl: sourceUrl,
                placedDate: placedDate,
                account: access.account,
                access: access,
                category: Category.findByAccessAndIdentity(access, categoryIdentity)
        )
        if (!entry.validate()) {

            System.err << "Failed entry validation: ${entry.errors}\n"
            return null
        }

        entry.save(flush: true)
        if (tagIdentities?.size()) access.tags.findAll {it.identity in tagIdentities}.each {
            entry.addToTags it
            it.addToEntries entry
            it.save()
        }
        entry.save(flush: true)
    }
}