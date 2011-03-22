package everfeeds

import everfeeds.manager.*

class Access {

    static final String TYPE_EVERNOTE = "evernote"
    static final String TYPE_GREADER = "greader"
    static final String TYPE_TWITTER = "twitter"

    static final Map MANAGERS = [
            (TYPE_EVERNOTE):EvernoteAccess,
            (TYPE_GREADER): GreaderAccess,
            (TYPE_TWITTER): TwitterAccess
    ]

    String identity
    String type
    String token
    String secret
    String shard

    boolean expired = false
    Date lastSync

    private accessManager

    Account account

    static belongsTo = Account
    static hasMany = [tags:Tag, categories:Category, entries:Entry]

    static transients = ["manager", "accessManager", "title"]

    static constraints = {
        identity unique: true
        secret nullable: true
        shard nullable: true
        account nullable: true
        lastSync nullable: true
        entries sort: "placedDate"
    }

    AAccess getManager() {
        if(!accessManager) {
            log.debug "Access | ${type}"
            accessManager = MANAGERS[type].newInstance(this)
        } else {
            log.debug "Access | ${type} already has ${accessManager.class.canonicalName}"
        }
        accessManager
    }

    String getTitle(){
        identity.substring(identity.indexOf(":")+1)
    }

    String toString() {
        "access#${identity}"
    }
}
