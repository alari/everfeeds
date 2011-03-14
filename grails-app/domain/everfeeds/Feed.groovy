package everfeeds

class Feed {

    Access access

    static belongsTo = [Access, Category, Tag]

    static hasMany = [tags:Tag, categories:Category]

    static constraints = {
    }
}
