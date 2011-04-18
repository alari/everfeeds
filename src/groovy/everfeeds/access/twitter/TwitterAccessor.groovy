package everfeeds.access.twitter

import com.twitter.Autolink
import everfeeds.Access
import everfeeds.access.Accessor
import everfeeds.envelops.EntryFace
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop
import java.text.SimpleDateFormat
import everfeeds.OAuthHelper
import everfeeds.access.Manager
import everfeeds.annotations.Reconnectable
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
                    check: {it?.user?.screen_name == access.title || it?.sender?.screen_name == access.title}
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
        Autolink autolink = new Autolink()

        String screenName
        String kind
        String sourceUrl

        List tags
        EntryFace entry

        CATEGORIES.each {catIdx, cat ->
            OAuthHelper.callJsonApi(config.oauth, cat + "?count=${num}&include_entities=1", access.token, access.secret)?.each {
                // Preparing strings which are different for PMs and regular twits
                kind = catIdx == "messages" ? "DM" : "twit"
                if(kind == "DM") {
                    screenName = it.sender.screen_name
                    sourceUrl = ""
                } else {
                    screenName = it?.user?.screen_name
                    sourceUrl = "http://twitter.com/${screenName}/status/${it.id}"
                }

                tags = []
                TAGS.each {tagId, tagData ->
                    if (tagData.check(it)) tags.add tagId
                }

                entry = new EntryEnvelop(
                        title: it.text,
                        content: autolink.autoLink(it.text),
                        imageUrl: it?.user?.profile_image_url ?: it.sender.profile_image_url,
                        identity: it.id,
                        kind: kind,
                        author: screenName,
                        tagIdentities: tags,
                        categoryIdentity: catIdx,
                        sourceUrl: sourceUrl,
                        placedDate: simpleDateFormat.parse(it?.created_at ?: it.sender.created_at),
                        accessId: access.id
                )
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
