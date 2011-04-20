package everfeeds.access.facebook

import everfeeds.access.Parser
import everfeeds.access.facebook.kind.FacebookStatus
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:04
 */
class FacebookParser extends Parser {

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    // here we should dispatch all kinds of facebook entries
    new FacebookStatus().newEntryEnvelop(node, accessor, categoryIdentity).buildEnvelop().entryEnvelop
  }
}
