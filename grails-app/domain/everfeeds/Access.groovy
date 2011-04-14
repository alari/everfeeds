package everfeeds

import everfeeds.access.*
import org.bson.types.ObjectId

class Access {
    static mapWith = "mongo"

    ObjectId id

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
        identity index: true, indexAttributes: [unique: true, dropDups: true]
        secret nullable: true
        shard nullable: true
        account nullable: true
        lastSync nullable: true
        entries sort: "placedDate"
        tags sort: "title", order: 1
        categories sort: "title", order: 1
    }

    Accessor getAccessor() {
        if(!cachedAccessor) {
            log.debug "Access | ${type}"
            cachedAccessor = Manager.getAccessor(type, this)
        } else {
            log.debug "Access | ${type} already has ${cachedAccessor.class.canonicalName}"
        }
        cachedAccessor
    }

    String toString() {
        "access#${identity}"
    }
}
