package everfeeds.access.facebook

import everfeeds.Access
import everfeeds.OAuthHelper
import everfeeds.access.Accessor
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.json.JSONElement

/**
 * @author alari
 * @since 14.03.11 14:55
 *
 * @author Boris G. Tsirkin
 */
class FacebookAccessor extends Accessor {

  static Logger log = Logger.getLogger(FacebookAccessor)

  static final Map<String, String> CATEGORIES = [
      news: "https://graph.facebook.com/me/home",
      events: "https://graph.facebook.com/me/events",
      wall: "https://graph.facebook.com/me/feed",
  ]

  FacebookAccessor(Access access) {
    this.access = access
  }

/*  public List<String> getKinds() {
    FacebookKindFactory.getKinds()
  }
   */

  List<CategoryEnvelop> getCategories() {
    List<CategoryEnvelop> categories = []
    CATEGORIES.keySet().each {
      categories.add new CategoryEnvelop(identity: it, title: "${type}.category.${it}", titleIsCode: true)
    }
    categories
  }

  List<TagEnvelop> getTags() {
    List<TagEnvelop> tags = []

    tags
  }

  boolean isPullable() {
    true
  }

  boolean isPushable() {
    false
  }

  public List<EntryEnvelop> pull(Map params = [:]) {
    log.debug "pulling from facebook"
    List<EntryEnvelop> entries = []

    EntryEnvelop entry

    CATEGORIES.each {catIdx, catUrl ->
      for (final JSONElement it in OAuthHelper.callJsonApi(config.oauth, catUrl, access.token, access.secret)?.data) {

        entry = parser.parseEntry(catIdx, it)
        if (params?.store) {
          entry.store()
        } else {
          entries.add entry
        }
      }
    }

    return entries
  }


}
