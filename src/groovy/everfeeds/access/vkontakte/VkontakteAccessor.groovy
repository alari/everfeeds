package everfeeds.access.vkontakte

import everfeeds.Access
import everfeeds.access.Accessor
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop
import org.apache.log4j.Logger

/**
 * @author Boris G. Tsirkin
 * @since 20.04.2011
 */
class VkontakteAccessor extends Accessor {

  static Logger log = Logger.getLogger(VkontakteAccessor)

  static final Map CATEGORIES = [:]

  VkontakteAccessor(Access access) {
    this.access = access
  }

  List<CategoryEnvelop> getCategories() {
    []
  }

  List<TagEnvelop> getTags() {
    List<TagEnvelop> tags = []

    tags
  }

  boolean isPullable() {
    // We are not yet able to pull from vk
    false
  }

  boolean isPushable() {
    false
  }

  public List<EntryEnvelop> pull(Map params = [:]) {
    List<EntryEnvelop> entries = []
    entries
  }
}
