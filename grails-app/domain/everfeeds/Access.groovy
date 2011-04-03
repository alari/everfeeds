package everfeeds

import everfeeds.access.*
import everfeeds.access.evernote.EvernoteAccessor
import everfeeds.access.gmail.GmailAccessor
import everfeeds.access.greader.GreaderAccessor
import everfeeds.access.twitter.TwitterAccessor

class Access {

    static final String TYPE_EVERNOTE = "evernote"
    static final String TYPE_GREADER = "greader"
    static final String TYPE_TWITTER = "twitter"
    static final String TYPE_GMAIL = "gmail"

    static final Map MANAGERS = [
            (TYPE_EVERNOTE):EvernoteAccessor,
            (TYPE_GREADER): GreaderAccessor,
            (TYPE_TWITTER): TwitterAccessor,
            (TYPE_GMAIL): GmailAccessor,
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

    SortedSet tags
    SortedSet categories

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
        tags sort: "title", order: 1
        categories sort: "title", order: 1
    }

    AAccessor getManager() {
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
