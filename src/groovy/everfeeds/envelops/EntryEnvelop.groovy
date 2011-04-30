package everfeeds.envelops

import everfeeds.Access
import everfeeds.Entry

/**
 * Created by alari @ 14.03.11 17:21
 */
class EntryEnvelop implements EntryFace {
  String identity
  String title
  String kind = ''
  String imageUrl
  String content
  String author
  String authorIdentity
  String sourceUrl
  Date placedDate

  List<String> tagIdentities
  String categoryIdentity

  int accessId

  Entry store() {
    Access access = Access.get(accessId)

    if (!Entry.isUnique(access, identity, kind)) return;

    Entry entry = new Entry(
        identity: identity,
        title: title,
        kind: kind,
        imageUrl: imageUrl,
        content: content,
        author: author,
        authorIdentity: authorIdentity,
        sourceUrl: sourceUrl,
        placedDate: placedDate,
        type: access.type
    )
    entry.access = access
    entry.account = access.account
    entry.categoryIdentity = categoryIdentity

    if (!entry.validate()) {
      System.err << "Failed entry validation: ${entry.errors}\n"
      return null
    }
    entry.save(flush: true)

    entry.tagIdentities = tagIdentities

    entry.save(flush: true)
  }

  String getType() {
    Access.get(accessId)?.type
  }
}
