package everfeeds

import everfeeds.access.Accessor

class Access {
  String identity
  String title
  String type
  String token
  String secret
  String shard

  boolean expired = false
  Date lastSync

  private cachedAccessor

  Account account

  SortedSet tags
  SortedSet categories

  static belongsTo = Account
  static hasMany = [tags: Tag, categories: Category]

  static transients = ["accessor", "cachedAccessor"]

  static constraints = {
    secret nullable: true
    shard nullable: true
    account nullable: true
    lastSync nullable: true
    tags sort: "title", order: 1
    categories sort: "title", order: 1
  }

  static mapping = {
    // TODO: add multifield unique key (type,identity)
    //type unique: "identity"
  }

  Accessor getAccessor() {
    if (!cachedAccessor) {
      cachedAccessor = Manager.getAccessor(type, this)
    }
    cachedAccessor
  }

  String toString() {
    "access#${identity}"
  }
}
