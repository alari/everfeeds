package everfeeds.access.facebook

import everfeeds.Access
import everfeeds.access.Accessor
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagEnvelop
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.json.JSONElement
import org.scribe.model.Verb

/**
 * @author alari
 * @since 14.03.11 14:55
 *
 * @author Boris G. Tsirkin
 */
class FacebookAccessor extends Accessor {

  static Logger log = Logger.getLogger(FacebookAccessor)
  static final GRAPH_URL = "https://graph.facebook.com"
  private static final POST_URL_TEMPLATE = GRAPH_URL + "/%s/feed"
  static final Map<String, String> CATEGORIES = [
      news: GRAPH_URL + "/me/home",
      events: GRAPH_URL +  "/me/events",
      wall: GRAPH_URL + "/me/feed",
  ]

  FacebookAccessor(Access access) {
    this.access = access
  }

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
    true
  }

  public List<EntryEnvelop> pull(Map params = [:]) {
    log.debug "pulling from facebook"
    List<EntryEnvelop> entries = []

    EntryEnvelop entry

    CATEGORIES.each {catIdx, catUrl ->
      for (final JSONElement it in callOAuthApiJSON(catUrl)?.data) {

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

  EntryEnvelop push(EntryFace entry) {
    def res = callOAuthApiJSON (
      String.format(POST_URL_TEMPLATE, access.identity),
      [
        message: entry.title, // todo: content!
        description: entry.description,
//        privacy: ([value: "EVERYONE"] as JSON)
      ]
    )
    if(res.containsKey("error")) {
      throw new Exception(res.error.toString())
    }

    parser.parseEntry("news", callOAuthApiJSON(GRAPH_URL, [id: res.id], Verb.GET))
  }

}
