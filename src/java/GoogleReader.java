/**
 * Created by alari @ 12.03.11 13:24
 */


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class GoogleReader {
    String email;
    String password;

    private static final String _AUTHPARAMS = "GoogleLogin auth=";
    private static final String _GOOGLE_LOGIN_URL = "https://www.google.com/accounts/ClientLogin";
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

    GoogleReader(String email, String password) {
        this.email = email ;
        this.password = password;
    }

    /**
     * Returns a Google Authentication Key
     * Requires a Google Username and Password to be sent in the POST headers
     * to http://www.google.com/accounts/ClientLogin
     *
     * @param GoogleGoogle_Username Google Username
     * @param Google_Password Google Password
     * @return Google authorisation token
     * @see getGoogleToken
     */
    public static String getGoogleAuthKey(String _USERNAME, String _PASSWORD) throws UnsupportedEncodingException, IOException {
        Document doc = Jsoup.connect(_GOOGLE_LOGIN_URL).data("accountType", "GOOGLE",
                "Email", _USERNAME,
                "Passwd", _PASSWORD,
                "service", "reader",
                "source", "&lt;your app name&gt;").userAgent("&lt;your app name&gt;").timeout(4000).post();

        // RETRIEVES THE RESPONSE TEXT inc SID and AUTH. We only want the AUTH key.
        String _AUTHKEY = doc.body().text().substring(doc.body().text().indexOf("Auth="), doc.body().text().length());
        _AUTHKEY = _AUTHKEY.replace("Auth=", "");
        return _AUTHKEY;
    }

    /**
     * Returns a Google Token
     * Requires a Google Username, Password and Auth key to be sent in the GET
     * to http://www.google.com/reader/api/0/token
     *
     * @param Google_Username Google Username
     * @param Google_Password Google Password
     * @return Google authorisation token
     * @see getGoogleAuthKey
     */
    public static String getGoogleToken(String _USERNAME, String _PASSWORD) throws UnsupportedEncodingException, IOException {
        Document doc = Jsoup.connect(_TOKEN_URL).header("Authorization", _AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD)).userAgent("&lt;your app name").timeout(4000).get();

        // RETRIEVES THE RESPONSE TOKEN
        String _TOKEN = doc.body().text();
        return _TOKEN;
    }

    /**
     * Returns Google Reader User Info
     * Requires a Google Username, Password and AUTH key to be sent in the POST
     * to http://www.google.com/reader/api/0/user-info
     *
     * @param GoogleGoogle_Username Google Username
     * @param Google_Password Google Password
     * @return Google Reader User Info
     * @see getGoogleToken
     */
    public static String getUserInfo(String _USERNAME, String _PASSWORD) throws UnsupportedEncodingException, IOException {
        Document doc = Jsoup.connect(_USER_INFO_URL).header("Authorization", _AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD)).userAgent("&lt;your app name&gt;").timeout(4000).get();

        // RETRIEVES THE RESPONSE USERINFO
        String _USERINFO = doc.body().text();
        return _USERINFO;
    }
    /**
     * Returns Google User ID
     * Requires a Google Username and Password to be sent in the POST headers
     * to http://www.google.com/accounts/ClientLogin
     *
     * @return Google User ID
     * @see getGoogleToken , getGoogleAuthKey
     */
    public static String getGoogleUserID(String _USERNAME, String _PASSWORD) throws UnsupportedEncodingException, IOException {
        /* USERINFO RETURNED LOOKS LIKE
        * {"userId":"14577161871823252783",
        * "userName":"Chris","userProfileId":"111618249557856839702",
        * "userEmail":"chrisdadswell@gmail.com",
        * "isBloggerUser":true,
        * "signupTimeSec":1159535065}
        */
        String _USERINFO = getUserInfo(_USERNAME, _PASSWORD);
        String _USERID = (String) _USERINFO.subSequence(11, 31);
        return _USERID;
    }
    /*
    public static String[] getTagList(String _USERNAME, String _PASSWORD) {
        Log.d(_APP_TAG, "METHOD: getTagList()");
        ArrayList & lt; String & gt; _TAGTITLE_ARRAYLIST = new ArrayList & lt; String & gt; ();
        String _TAG_LABEL = null;
        try {
            _TAG_LABEL = "user/" + getGoogleUserID(_USERNAME, _PASSWORD) + "/label/";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = Jsoup.connect(_TAG_LIST_URL).header("Authorization", _AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD)).userAgent("&lt;your app name&gt;").timeout(6000).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Elements links = doc.select("string");
        for (Element link: links) {
            String tagAttrib = link.attr("name");
            String tagText = link.text();
            if (Func_Strings.FindWordInString(tagText, _TAG_LABEL)) {
                _TAGTITLE_ARRAYLIST.add(tagText.substring(32));
            }
        }

        String[] _TAGTITLE_ARRAY = new String[_TAGTITLE_ARRAYLIST.size()];
        _TAGTITLE_ARRAYLIST.toArray(_TAGTITLE_ARRAY);
        return _TAGTITLE_ARRAY;
    }

    public static String[] getSubList(String _USERNAME, String _PASSWORD) throws UnsupportedEncodingException, IOException {
        ArrayList & lt; String & gt; _SUBTITLE_ARRAYLIST = new ArrayList & lt; String & gt; ();

        Document doc = Jsoup.connect(_SUBSCRIPTION_LIST_URL).header("Authorization", _AUTHPARAMS + getGoogleAuthKey(_USERNAME, _PASSWORD)).userAgent("&lt;your app name&gt;").timeout(5000).get();

        Elements links = doc.select("string");
        for (Element link: links) {
            String tagAttrib = link.attr("name");
            String tagText = link.text();
            _SUBTITLE_ARRAYLIST.add(tagText);
        }

        String[] _SUBTITLE_ARRAY = new String[_SUBTITLE_ARRAYLIST.size()];
        _SUBTITLE_ARRAYLIST.toArray(_SUBTITLE_ARRAY);
        return _SUBTITLE_ARRAY;
    }*/
}
