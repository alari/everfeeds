package everfeeds.access.gmail

import everfeeds.access.Parser
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
    new EntryEnvelop(
        title: node.title.text(),
        content: node.summary.text(),
        identity: node.id,
        author: node.author.name.text() + " &lt;" + node.author.email.text() + "&gt;",
        categoryIdentity: "inbox-unread",
        sourceUrl: node.link.@href,
        placedDate: new Date(),
        accessId: access.id
    )
  }
}
