package everfeeds.access.facebook

import com.twitter.Autolink
import everfeeds.Access
import everfeeds.OAuthHelper
import everfeeds.access.AAccessor
import everfeeds.access.IEntry
import everfeeds.access.envelops.CategoryEnvelop
import everfeeds.access.envelops.EntryEnvelop
import everfeeds.access.envelops.TagEnvelop
import java.text.SimpleDateFormat

/**
 * Created by alari @ 14.03.11 14:55
 */
class FacebookAccessor extends AAccessor {

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

    void push(IEntry entry) {

    }
}
