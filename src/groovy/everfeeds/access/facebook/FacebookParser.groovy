package everfeeds.access.facebook

import everfeeds.access.Parser
import everfeeds.envelops.EntryEnvelop
import java.text.SimpleDateFormat

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:04
 */
class FacebookParser extends Parser {
  private static DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    String screenName = node.from?.name ?: "Unknown"
    new EntryEnvelop(
        title: node?.caption,
        kind: node?.type,
        content: (node?.message ?: "") + " " + (node?.description ?: ""),
        imageUrl: node?.picture ?: node?.icon ?: "http://facebook.com/${it.id}/picture",
        identity: node.id,
        author: screenName,
        tagIdentities: [],
        categoryIdentity: categoryIdentity,
        sourceUrl: node.source ?: (node.link ?: "http://facebook.com/${it.id}"),
        placedDate: (node?.created_time) ? DATE_FORMAT.parse(node?.created_time) : new Date(),
        accessId: access.id
    )
  }
}
