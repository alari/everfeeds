package everfeeds

class Access {

    static final String TYPE_EVERNOTE = "en"
    static final String TYPE_GREADER = "gr"

    String identity
    String type
    String token
    String secret
    String shard

    boolean expired = false

    Account account

    static belongsTo = Account
    static hasMany = [feeds:Feed]

    static constraints = {
        identity unique: true
        secret nullable: true
        shard nullable: true
        account nullable: true
    }
}
