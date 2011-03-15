package everfeeds

class Feed {

    Access access
    Category category

    static belongsTo = [Access, Category, Tag]

    static hasMany = [tags:Tag]

    static constraints = {
    }

    String toString(){
        "${category.title} in ${access.identity}, tags ${tags*.title.join(", ")}"
    }
}
