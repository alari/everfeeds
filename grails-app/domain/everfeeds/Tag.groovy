package everfeeds

import everfeeds.envelops.TagFace

class Tag implements TagFace, Comparable {
  String identity
  String title
  boolean titleIsCode = false

  Access access

  static belongsTo = Access

  static constraints = {
    title index: "titleTagIdx"
  }

  static mapping = {
    sort "title"
  }

  String toString() {
    if (titleIsCode) return I18n._."${title}"
    title
  }

  public int compareTo(def other) {
    if (other instanceof Tag) {
      return title <=> other.title
    }
    throw new IllegalArgumentException("Cannot compare with ${other}")
  }
}
