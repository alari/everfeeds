package everfeeds.envelops

/**
 * Created by alari @ 14.03.11 16:34
 */
class CategoryEnvelop implements CategoryFace {
  String identity
  String title
  boolean titleIsCode = false

  def original

  String toString() {
    "(${identity} -> ${title})"
  }
}
