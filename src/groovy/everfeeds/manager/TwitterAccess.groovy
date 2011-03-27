package everfeeds.manager

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import everfeeds.Access
import everfeeds.AuthService
import java.text.SimpleDateFormat
import com.twitter.Autolink

/**
 * Created by alari @ 14.03.11 14:55
 */
class TwitterAccess extends AAccess {

    private config = AH.application.mainContext.grailsApplication.config.twitter

    static final String _FRIENDS_TIMELINE_URL = "http://api.twitter.com/1/statuses/friends_timeline.json"

    TwitterAccess(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories(){
        List<CategoryEnvelop> categories = []
        categories.add new CategoryEnvelop(identity: "friends", title: "friends")
        categories
    }

    List<TagEnvelop> getTags(){
        List<TagEnvelop> tags = []
        tags
    }

    boolean isPullable(){
        true
    }

    boolean isPushable(){
        true
    }

    public List<EntryEnvelop> pull(Map params=[:]){
        // TODO: handle categories and tags somehow
        // Max count
        int num = params.num ?: NUM

        List<EntryEnvelop> entries = []

        AuthService service = AH.application.mainContext.authService
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
        Autolink autolink = new Autolink()

        service.oAuthCallJson(_FRIENDS_TIMELINE_URL+"?count=${num}", config, access.token, access.secret)?.each{
            entries.add new EntryEnvelop(
                    title: it.text,
                    content: autolink.autoLink(it.text),
                    imageUrl: it.user.profile_image_url,
                    identity: it.id,
                    author: it.user.screen_name,
                    /*tagIdentities: it.tagGuids,*/
                    categoryIdentity: "friends",
                    sourceUrl: "http://twitter.com/${it.user.screen_name}/status/${it.id}",
                    placedDate: simpleDateFormat.parse(it.created_at),
                    accessId: access.id
            )
        }

        entries
    }

    void push(IEntry entry){

    }
}
