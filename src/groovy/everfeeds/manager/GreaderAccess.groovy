package everfeeds.manager

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import everfeeds.Access
import everfeeds.OAuthSession

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

    def getCategories(){
        OAuthSession s = new OAuthSession(config);
        s.consumer.setTokenWithSecret(access.token, access.secret)
        [
                s.apiGet(_SUBSCRIPTION_LIST_URL+"?"),
                s.apiGet(config.emailUrl),
                s.apiGet(_TAG_LIST_URL)
        ]
    }

    def getTags(){
    }

    def getTags(category){
    }

    void sync(){

    }

    boolean isPullable(){
        true
    }

    boolean isPushable(){
        true
    }

    def pull(){

    }

    def push(){

    }


}
