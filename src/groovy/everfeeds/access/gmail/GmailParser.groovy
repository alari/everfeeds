package everfeeds.access.gmail

import everfeeds.access.Parser
import everfeeds.access.gmail.kind.GmailEmail
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:02
 */
class GmailParser extends Parser {
  EntryEnvelop parseEntry(node) {
    parseEntry "inbox-unread", node
  }

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    new GmailEmail().newEntryEnvelop(node, accessor, categoryIdentity).buildEnvelop().entryEnvelop
  }
}
