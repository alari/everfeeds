package everfeeds.manager

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import everfeeds.Access
import everfeeds.OAuthSession
import grails.converters.deep.JSON

/**
 * Created by alari @ 14.03.11 14:55
 */
class GreaderAccess extends AAccess {

    private static final String _READER_BASE_URL = "http://www.google.com/reader/";
    private static final String _API_URL = _READER_BASE_URL + "api/0/";
    private static final String _TOKEN_URL = _API_URL + "token";
    private static final String _USER_INFO_URL = _API_URL + "user-info";
    private static final String _USER_LABEL = "user/-/label/";
    private static final String _TAG_LIST_URL = _API_URL + "tag/list";
    private static final String _EDIT_TAG_URL = _API_URL + "tag/edit";
    private static final String _RENAME_TAG_URL = _API_URL + "rename-tag";
    private static final String _DISABLE_TAG_URL = _API_URL + "disable-tag";
    private static final String _SUBSCRIPTION_URL = _API_URL + "subscription/edit";
    private static final String _SUBSCRIPTION_LIST_URL = _API_URL + "subscription/list";

    private config = AH.application.mainContext.grailsApplication.config.greader


    GreaderAccess(Access access) {
        this.access = access
    }

    List<CategoryEnvelop> getCategories(){
        List<CategoryEnvelop> categories = []

        apiGet(_SUBSCRIPTION_LIST_URL)?.subscriptions?.each{
            categories.add new CategoryEnvelop(identity: it.id, title: it.title, original: it)
        }

        categories
    }

    List<TagEnvelop> getTags(){
        List<TagEnvelop> tags = []

        apiGet(_TAG_LIST_URL)?.tags?.each{
            tags.add new TagEnvelop(identity: it.id, title: it.id, original: it)
        }

        tags
    }

    def getTags(category){
    }

    void sync(){

    }

    boolean isPullable(){
        true
    }

    boolean isPushable(){
        false
    }

    def pull(){

    }

    def push(){

    }

    private apiGet(String url) {
        OAuthSession s = new OAuthSession(config);
        s.consumer.setTokenWithSecret(access.token, access.secret)

        url += (url.indexOf("?")>-1 ? "&" : "?")+"output=json"
        String result = s.apiGet(url)

        if(!result) {
            access.expired = true
            access.save()
            return [:]
        }
        JSON.parse(result)
    }
}