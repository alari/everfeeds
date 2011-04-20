package everfeeds.access.twitter

import everfeeds.access.Kind
import everfeeds.access.Parser
import everfeeds.access.twitter.kind.TwitterDm
import everfeeds.access.twitter.kind.TwitterStatus
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 0:31
 */
class TwitterParser extends Parser {
  EntryEnvelop parseEntry(String categoryIdentity, Object node) {
    Kind kind
    if (categoryIdentity == "messages") {
      kind = new TwitterDm()
    } else {
      kind = new TwitterStatus()
    }
    kind.newEntryEnvelop(node, accessor, categoryIdentity).buildEnvelop().entryEnvelop
  }
}
