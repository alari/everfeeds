package everfeeds

import everfeeds.access.ICategory

class Category implements ICategory,Comparable{

    String identity
    String title

    Access access

    static belongsTo = Access

    static hasMany = [entries:Entry]

    static constraints = {
        entries sort: "placedDate", order: -1
        title index: "titleCategoryIdx"
    }

    static mapping = {
        sort "title"
    }

    String toString(){
        "[${identity} -> ${title}]"
    }

    public int compareTo(def other) {
        if(other instanceof Category) {
            return title <=> other.title
        }
        throw new IllegalArgumentException("Cannot compare with ${other}")
    }
}
