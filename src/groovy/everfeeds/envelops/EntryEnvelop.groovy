package everfeeds.envelops

import everfeeds.Access
import everfeeds.Category
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
  String sourceUrl
  Date placedDate

  List<String> tagIdentities
  String categoryIdentity

  int accessId

  Entry store() {
    Access access = Access.get(accessId)
    // Check uniqueness
    if (Entry.createCriteria().count {
      and {
        eq "access", access
        eq "identity", identity
        eq "kind", kind
      }
    }) return;

    Entry entry = new Entry(
        identity: identity,
        title: title,
        kind: kind,
        imageUrl: imageUrl,
        content: content,
        author: author,
        sourceUrl: sourceUrl,
        placedDate: placedDate,
        account: access.account,
        access: access,
        type: access.type
    )
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
