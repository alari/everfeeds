package everfeeds.access.twitter

import everfeeds.Access
import everfeeds.OAuthHelper
import everfeeds.access.Accessor
import everfeeds.access.Manager
import everfeeds.annotations.Reconnectable
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagEnvelop
import org.apache.log4j.Logger
import org.scribe.model.Verb

/**
 * Created by alari @ 14.03.11 14:55
 */
class TwitterAccessor extends Accessor {

  static Logger log = Logger.getLogger(TwitterAccessor)

  static final Map CATEGORIES = [
      timeline: "http://api.twitter.com/1/statuses/home_timeline.json",
      mentions: "http://api.twitter.com/1/statuses/mentions.json",
      messages: "http://api.twitter.com/1/direct_messages.json",
  ]

  Map TAGS = [
      own: [
          title: "Typed by you",
          check: {access.identity in [it?.user?.id?.toString(), it?.sender?.id?.toString()]}
      ],
      retweet: [
          title: "Retweets",
          check: {it?.retweeted_status}
      ],
      reply: [
          title: "Replies",
          check: {it?.in_reply_to_user_id && !it.isNull("in_reply_to_user_id")}
      ],
      withMentions: [
          title: "With any @mentions",
          check: {it?.entities?.user_mentions?.size()}
      ],
      mention: [
          title: "Mentions of you",
          check: {it?.entities?.user_mentions?.any {m -> m?.id?.toString() == access.identity}}
      ],
      link: [
          title: "Contains links",
          check: {it?.entities?.urls?.size()}
      ],
      hash: [
          title: "Contains hashtags",
          check: {it?.entities?.hashtags?.size()}
      ],
      favorites: [
          title: "Favorited by you",
          check: {it?.favorited && it.getBoolean("favorited")}
      ]
  ]

  TwitterAccessor(Access access) {
    this.access = access
  }

  List<CategoryEnvelop> getCategories() {
    List<CategoryEnvelop> categories = []
    categories.add new CategoryEnvelop(identity: "timeline", title: "${type}.category.timeline", titleIsCode: true)
    categories.add new CategoryEnvelop(identity: "mentions", title: "${type}.category.mentions", titleIsCode: true)
    categories.add new CategoryEnvelop(identity: "messages", title: "${type}.category.messages", titleIsCode: true)
    categories
  }

  List<TagEnvelop> getTags() {
    List<TagEnvelop> tags = []
    TAGS.each {identity, params ->
      tags.add new TagEnvelop(identity: identity, title: "${type}.tag.${identity}", titleIsCode: true)
    }
    tags
  }

  boolean isPullable() {
    true
  }

  boolean isPushable() {
    true
  }

  @Reconnectable
  public List<EntryEnvelop> pull(Map params = [:]) {
    // Max count
    int num = params.num ?: Manager.NUM

    List<EntryEnvelop> entries = []

    String screenName
    String kind
    String sourceUrl

    List tags
    EntryFace entry

    //TODO: handle categories!
    CATEGORIES.each {catIdx, cat ->
      callOAuthApiJSON(cat + "?count=${num}&include_entities=1")?.each {
        entry = parser.parseEntry(catIdx.toString(), it)

        if (params?.store) {
          entry.store()
        } else {
          entries.add entry
        }
      }
    }

    entries
  }

  EntryEnvelop push(EntryFace entry) {
    String status = entry.title
    if(status.size() > 140) status = status.substring(0, 140)
    if(entry.sourceUrl && status.size() + entry.sourceUrl.size() < 140) status += " "+entry.sourceUrl

    def res = callOAuthApiJSON(
        "http://api.twitter.com/1/statuses/update.json",
        [status: status]
    )
    if(res.containsKey("error")) {
      throw new Exception(res.error.toString())
    }

    parser.parseEntry("timeline", res)
  }
}
