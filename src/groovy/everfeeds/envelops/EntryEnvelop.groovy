package everfeeds.envelops

import everfeeds.Access
import everfeeds.Entry
import everfeeds.access.Manager

/**
 * Created by alari @ 14.03.11 17:21
 */
class EntryEnvelop implements EntryFace {
  String identity
  String title
  String kind = ''
  String imageUrl
  String description
  String content
  String author
  String authorIdentity
  boolean accessIsAuthor
  boolean isPublic
  String sourceUrl
  Date placedDate

  List<String> tagIdentities
  String categoryIdentity

  int accessId

  Entry store() {
    Access access = Access.get(accessId)

    Entry entry

    if (!Entry.isUnique(access, identity, kind)) {
      entry = Entry.createCriteria().get {
        and {
          eq "accessId", access.id
          eq "identity", identity
          eq "kind", kind
        }
      }
    } else {
      entry = new Entry(type: access.type)
      entry.access = access
      entry.account = access.account
    }

    ['identity',
        'title',
        'kind',
        'imageUrl',
        'description',
        'content',
        'author',
        'authorIdentity',
        'accessIsAuthor',
        'isPublic',
        'sourceUrl',
        'placedDate'].each {
      entry."${it}" = this."${it}"
    }



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

  Class getKindClass() {
    Manager.classForKind(type, kind)
  }
}
