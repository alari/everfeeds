package everfeeds.access.twitter

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import com.twitter.Autolink
import everfeeds.Access
import everfeeds.AuthService
import everfeeds.access.AAccess
import everfeeds.access.IEntry
import everfeeds.access.envelops.CategoryEnvelop
import everfeeds.access.envelops.EntryEnvelop
import everfeeds.access.envelops.TagEnvelop
import java.text.SimpleDateFormat

/**
 * Created by alari @ 14.03.11 14:55
 */
class TwitterAccess extends AAccess {

    private config = AH.application.mainContext.grailsApplication.config.twitter

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

    TwitterAccess(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories() {
        List<CategoryEnvelop> categories = []
        categories.add new CategoryEnvelop(identity: "timeline", title: "Timeline")
        categories.add new CategoryEnvelop(identity: "mentions", title: "Mentions")
        categories.add new CategoryEnvelop(identity: "messages", title: "Messages")
        categories
    }

    List<TagEnvelop> getTags() {
        List<TagEnvelop> tags = []
        TAGS.each {identity, params ->
            tags.add new TagEnvelop(identity: identity, title: params.title)
        }
        tags
    }

    boolean isPullable() {
        true
    }

    boolean isPushable() {
        true
    }

    public List<EntryEnvelop> pull(Map params = [:]) {
        // TODO: handle categories and tags somehow
        // Max count
        int num = params.num ?: everfeeds.access.AAccess.NUM

        List<EntryEnvelop> entries = []

        AuthService service = AH.application.mainContext.authService
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
        Autolink autolink = new Autolink()

        String screenName
        List tags
        IEntry entry

        CATEGORIES.each {catIdx, cat ->
            service.oAuthCallJson(cat + "?count=${num}&include_entities=1", config, access.token, access.secret)?.each {
                tags = []
                screenName = it?.user?.screen_name ?: it.sender.screen_name
                TAGS.each {tagId, tagData ->
                    if (tagData.check(it)) tags.add tagId
                }
                entry = new EntryEnvelop(
                        title: it.text,
                        content: autolink.autoLink(it.text),
                        imageUrl: it?.user?.profile_image_url ?: it.sender.profile_image_url,
                        identity: (catIdx == "messages" ? catIdx : "") + it.id,
                        author: screenName,
                        tagIdentities: tags,
                        categoryIdentity: catIdx,
                        sourceUrl: "http://twitter.com/${screenName}/status/${it.id}",
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

    void push(IEntry entry) {

    }
}
