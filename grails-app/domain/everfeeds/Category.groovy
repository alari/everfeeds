package everfeeds

import everfeeds.manager.ICategory

class Category implements ICategory{

    String identity
    String title

    Access access

    static belongsTo = Access

    static hasMany = [entries:Entry]

    static constraints = {
        entries sort: "placedDate", order: -1
    }

    String toString(){
        "[${identity} -> ${title}]"
    }
}
