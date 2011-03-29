package everfeeds

import everfeeds.manager.ITag

class Tag implements ITag, Comparable{
    String identity
    String title

    Access access

    static belongsTo = Access

    static hasMany = [entries:Entry]

    static constraints = {
        title index: "titleTagIdx"
    }

    static mapping = {
        sort "title"
    }

    String toString(){
        "[${identity} -> ${title}]"
    }

    public int compareTo(def other) {
        if(other instanceof Tag) {
            return title <=> other.title
        }
        throw new IllegalArgumentException("Cannot compare with ${other}")
    }
}
