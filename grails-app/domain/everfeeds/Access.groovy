package everfeeds

import everfeeds.access.*

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
    static hasMany = [tags:Tag, categories:Category, entries:Entry]

    static transients = ["accessor", "cachedAccessor"]

    static constraints = {
        secret nullable: true
        shard nullable: true
        account nullable: true
        lastSync nullable: true
        entries sort: "placedDate"
        tags sort: "title", order: 1
        categories sort: "title", order: 1
      // TODO: add composite unique key (identity, type)
    }

    Accessor getAccessor() {
        if(!cachedAccessor) {
            cachedAccessor = Manager.getAccessor(type, this)
        }
        cachedAccessor
    }

    String toString() {
        "access#${identity}"
    }
}
