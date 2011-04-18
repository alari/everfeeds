package everfeeds.access.facebook

import everfeeds.Access
import everfeeds.OAuthHelper
import everfeeds.access.Accessor
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagEnvelop

import org.apache.log4j.Logger
import java.text.SimpleDateFormat

/**
 * @author alari
 * @since 14.03.11 14:55
 *
 * @author Boris G. Tsirkin
 */
class FacebookAccessor extends Accessor {
  private static DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  static Logger log = Logger.getLogger(FacebookAccessor)

  static final Map CATEGORIES = [
    news: "https://graph.facebook.com/me/home",
    events: "https://graph.facebook.com/me/events",
    wall: "https://graph.facebook.com/me/feed",
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
    false
  }

  public List<EntryEnvelop> pull(Map params = [:]) {
    log.debug "pulling from facebook"
    List<EntryEnvelop> entries = []

    List<String> tagList = []
    String screenName

    EntryEnvelop entry

    CATEGORIES.each {catIdx, catUrl ->
      for (it in OAuthHelper.callJsonApi(config.oauth, catUrl.toString(), access.token, access.secret)?.data) {

        screenName = it.from?.name ?: "Unknown"

        log.warn tagList
        entry = new EntryEnvelop(
          title: it?.caption,
          kind: it?.type,
          content: it?.message + " " + it?.description,
          imageUrl: it?.picture ?: it?.icon ?: "",
          identity: it.id,
          author: screenName,
          tagIdentities: tagList,
          categoryIdentity: catIdx,
          sourceUrl: it.source ?: (it.link ?: "http://facebook.com/${it.id}"),
          placedDate: (it?.created_time) ? DATE_FORMAT.parse(it?.created_time) : new Date(),
          accessId: access.id
        )
        if (params?.store) {
          entry.store()
        } else {
          entries.add entry
        }
      }
    }

    return entries
  }


  void push(EntryFace entry) {

  }
}
