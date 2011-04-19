package everfeeds.access.twitter

import com.twitter.Autolink
import everfeeds.access.Parser
import everfeeds.envelops.EntryEnvelop
import java.text.SimpleDateFormat

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 0:31
 */
class TwitterParser extends Parser {

  static private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
  static private Autolink autolink = new Autolink()

  EntryEnvelop parseEntry(String categoryIdentity, Object node) {
    // Preparing strings which are different for PMs and regular twits
    String kind = categoryIdentity == "messages" ? "DM" : "tweet"
    String screenName
    String sourceUrl
    List<String> tags
    if (kind == "DM") {
      screenName = node.sender.screen_name
      sourceUrl = ""
    } else {
      screenName = node?.user?.screen_name
      sourceUrl = "http://twitter.com/${screenName}/status/${it.id}"
    }

    tags = []
    accessor.TAGS.each {tagId, tagData ->
      if (tagData.check(node)) tags.add tagId
    }

    new EntryEnvelop(
        title: node.text,
        content: autolink.autoLink(node.text),
        imageUrl: node?.user?.profile_image_url ?: node.sender.profile_image_url,
        identity: node.id,
        kind: kind,
        author: screenName,
        tagIdentities: tags,
        categoryIdentity: categoryIdentity,
        sourceUrl: sourceUrl,
        placedDate: simpleDateFormat.parse(node?.created_at ?: node.sender.created_at),
        accessId: access.id
    )
  }
}
