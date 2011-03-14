package everfeeds

class Tag {

    Access access

    String identity
    String title

    static hasMany = [feeds:Feed]

    static belongsTo = Access

    static constraints = {
    }
}
