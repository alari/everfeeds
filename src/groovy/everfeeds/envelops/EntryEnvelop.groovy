package everfeeds.envelops

import everfeeds.Access
import everfeeds.Entry
import everfeeds.Manager
import org.apache.log4j.Logger

/**
 * Created by alari @ 14.03.11 17:21
 */
class EntryEnvelop implements EntryFace {
  private static Logger log = Logger.getLogger(EntryEnvelop)
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

  private Map schemalessParams = [:]

  void putAt(String name, value) {
    if(this.class.properties.containsKey(name)) {
      this."${name}" = value
    } else {
      schemalessParams[name] = value
    }
  }

  def getAt(String name) {
    if(schemalessParams.containsKey(name)) {
      return schemalessParams[name]
    } else if(this.class.properties.containsKey(name)) {
      return this."${name}"
    }
    return null
  }

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
      log.error("Failed entry validation: ${entry.errors}")
      return null
    }
    entry.save(flush: true)

    entry.tagIdentities = tagIdentities
    schemalessParams.each{k,v -> entry[k] = v}

    entry.save(flush: true)
  }

  String getType() {
    Access.get(accessId)?.type
  }

  Class getKindClass() {
    Manager.classForKind(type, kind)
  }
}
