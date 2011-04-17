package everfeeds

import everfeeds.envelops.CategoryFace

class Category implements CategoryFace,Comparable{

    String identity
    String title
    boolean titleIsCode = false

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
        if(titleIsCode) return I18n._."${title}"
        title
    }

    public int compareTo(def other) {
        if(other instanceof Category) {
            return title <=> other.title
        }
        throw new IllegalArgumentException("Cannot compare with ${other}")
    }
}
