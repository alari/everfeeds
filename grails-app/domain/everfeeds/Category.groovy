package everfeeds

class Category {

    Access access

    String identity
    String title

    static hasMany = [feeds: Feed]

    static belongsTo = Access

    static constraints = {
    }

    String toString(){
        "[${identity} -> ${title}]"
    }
}
