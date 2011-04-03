package everfeeds

import everfeeds.access.*

class Access {

    static final String TYPE_EVERNOTE = "evernote"
    static final String TYPE_GREADER = "greader"
    static final String TYPE_TWITTER = "twitter"
    static final String TYPE_GMAIL = "gmail"

    String identity
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

    static transients = ["accessor", "cachedAccessor", "title"]

    static constraints = {
        identity unique: true
        secret nullable: true
        shard nullable: true
        account nullable: true
        lastSync nullable: true
        entries sort: "placedDate"
        tags sort: "title", order: 1
        categories sort: "title", order: 1
    }

    AAccessor getAccessor() {
        if(!cachedAccessor) {
            log.debug "Access | ${type}"
            cachedAccessor = Manager.getAccessor(type, this)
        } else {
            log.debug "Access | ${type} already has ${cachedAccessor.class.canonicalName}"
        }
        cachedAccessor
    }

    String getTitle(){
        identity.substring(identity.indexOf(":")+1)
    }

    String toString() {
        "access#${identity}"
    }
}
