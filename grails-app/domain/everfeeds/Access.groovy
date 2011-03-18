package everfeeds

import everfeeds.manager.*

class Access {

    static final String TYPE_EVERNOTE = "evernote"
    static final String TYPE_GREADER = "greader"

    static final Map MANAGERS = [
            (TYPE_EVERNOTE):EvernoteAccess,
            (TYPE_GREADER): GreaderAccess
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
    static hasMany = [feeds:Feed, tags:Tag, categories:Category]

    static transients = ["manager", "accessManager", "title"]

    static constraints = {
        identity unique: true
        secret nullable: true
        shard nullable: true
        account nullable: true
        lastSync nullable: true
    }

    AAccess getManager() {
        if(!accessManager) {
            accessManager = MANAGERS[type].newInstance(this)
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
