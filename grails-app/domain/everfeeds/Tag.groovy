package everfeeds

import everfeeds.manager.ITag

class Tag implements ITag{
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
