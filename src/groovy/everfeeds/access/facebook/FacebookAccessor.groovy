package everfeeds.access.facebook

import everfeeds.Access

import everfeeds.access.Accessor
import everfeeds.envelops.EntryFace
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop

/**
 * Created by alari @ 14.03.11 14:55
 */
class FacebookAccessor extends Accessor {

    FacebookAccessor(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories() {
        List<CategoryEnvelop> categories = []

        categories
    }

    List<TagEnvelop> getTags() {
        List<TagEnvelop> tags = []

        tags
    }

    boolean isPullable() {
        false
    }

    boolean isPushable() {
        false
    }

    public List<EntryEnvelop> pull(Map params = [:]) {
        List<EntryEnvelop> entries = []
        return entries
    }

    void push(EntryFace entry) {

    }
}
