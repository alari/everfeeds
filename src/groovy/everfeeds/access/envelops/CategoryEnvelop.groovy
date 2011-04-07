package everfeeds.access.envelops

import everfeeds.access.ICategory

/**
 * Created by alari @ 14.03.11 16:34
 */
class CategoryEnvelop implements ICategory {
    String identity
    String title
    boolean titleIsCode = false

    def original

    String toString() {
        "(${identity} -> ${title})"
    }
}
