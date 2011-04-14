package everfeeds

import everfeeds.envelops.TagFace
import org.bson.types.ObjectId

class Tag implements TagFace, Comparable{

    static mapWith = "mongo"

    ObjectId id

    String authenticity
    String title
    boolean titleIsCode = false

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
        if(titleIsCode) return I18n."${title}"()
        title
    }

    public int compareTo(def other) {
        if(other instanceof Tag) {
            return title <=> other.title
        }
        throw new IllegalArgumentException("Cannot compare with ${other}")
    }
}
