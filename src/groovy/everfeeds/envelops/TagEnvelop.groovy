package everfeeds.envelops

/**
 * Created by alari @ 14.03.11 16:47
 */
class TagEnvelop implements TagFace {
  String identity
  String title
  boolean titleIsCode = false
  def original

  String toString() {
    "(${identity} -> ${title})"
  }
}
