package everfeeds

import everfeeds.manager.ICategory

class Category implements ICategory{

    String identity
    String title

    Access access

    static hasMany = [feeds: Feed]

    static belongsTo = Access

    static constraints = {
    }

    String toString(){
        "[${identity} -> ${title}]"
    }
}
