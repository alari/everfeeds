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
          check: {it?.user?.id == access.identity || it?.sender?.id == access.identity}
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
          check: {it?.entities?.user_mentions?.any {m -> m?.screen_name == access.title}}
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
      OAuthHelper.callJsonApi(config.oauth, cat + "?count=${num}&include_entities=1", access.token, access.secret)?.each {
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

  void push(EntryFace entry) {

  }
}
