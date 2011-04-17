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
    // Logger is usually initiated in Grails as lowercased log
    // "this" in static context means "this class", even if IDEA can't understand it
  static Logger log = Logger.getLogger(FacebookAccessor)
  private static enum TYPE {
    VIDEO ("Video"),
    LINK ("Link"),
    STATUS ("Status");

    final String description

    private TYPE (final String description) {
      this.description = description;
    }

    public check(final String st) {
      return st.equalsIgnoreCase(this.description);
    }

  }

  static final Map CATEGORIES = [
    news: "https://graph.facebook.com/me/home",
    events: "https://graph.facebook.com/me/events",
    wall: "https://graph.facebook.com/me/feed",
  ]

  static final Map<String, Map> TAGS;
  static {
    Map tags = new HashMap<String, Map>();
    TYPE.values().each {val ->
      final name = val.description.toLowerCase()
      Map map = new HashMap<String, Closure> ();
      map.put("title", { val });
      map.put("check", { val.check(it?.status?:"") });
      tags.put("${name}s", map)
    }
    TAGS = tags;
  }

  FacebookAccessor(Access access) {
    this.access = access
  }

  List<CategoryEnvelop> getCategories() {
    final List<CategoryEnvelop> categories = []
    categories.add new CategoryEnvelop(identity: "events", title: "fb.category.events", titleIsCode: true)
    categories.add new CategoryEnvelop(identity: "news", title: "fb.category.news", titleIsCode: true)
    categories.add new CategoryEnvelop(identity: "wall", title: "fb.category.wall", titleIsCode: true)
    categories
  }

  List<TagEnvelop> getTags() {
    List<TagEnvelop> tags = []
    TAGS.each {identity, params ->
      tags.add (new TagEnvelop(identity: identity, title: "fb.tag."+identity, titleIsCode: true))
    }
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

    CATEGORIES.each {catIdx, cat ->
      for (it in OAuthHelper.callJsonApi(config.oauth, String.valueOf(cat), access.token, access.secret)?.data) {
        log.debug("category ${cat}")
        def tagList = []
        def screenName = it?.user?.screen_name ?: it["from"].name

        TAGS.each {tagId, tagData ->
          if (tagData.check(it)) {
            tagList.add (tagId)
            log.warn tagData
          }
        }
        log.warn tagList
        def entry = new EntryEnvelop(
          title: it?.caption,
          kind: it?.type,
          content: it?.message + " " + it?.description,
          imageUrl: it?.picture ?: it?.icon ?: "",
          identity: it.id,
          author: screenName ?: "Unknown",
          tagIdentities: tagList,
          categoryIdentity: catIdx,
          sourceUrl: it.source ?: (it.link ?: "http://facebook.com/${screenName}"),
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
