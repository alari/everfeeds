package everfeeds.access.greader

import everfeeds.access.Parser
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 0:17
 */
class GreaderParser extends Parser {

  EntryEnvelop parseEntry(node) {
    parseEntry node.origin.streamId, node
  }

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    new EntryEnvelop(
        title: node.title,
        content: node.content?.content ?: node.summary?.content?.replace("\n", "<br/>"),
        identity: node.id,
        author: node.author,
        tagIdentities: node.categories.collect {it.toString()},
        categoryIdentity: categoryIdentity,
        sourceUrl: node.alternate.find {it.type == "text/html"}?.href,
        placedDate: new Date(((long) node.updated) * 1000),
        accessId: access.id
    )
  }

}
