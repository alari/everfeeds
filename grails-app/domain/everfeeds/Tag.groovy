package everfeeds

import everfeeds.manager.ITag

class Tag implements ITag{
    String identity
    String title

    Access access

    static belongsTo = Access

    static hasMany = [entries:Entry]

    static constraints = {
    }

    String toString(){
        "[${identity} -> ${title}]"
    }
}
