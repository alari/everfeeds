package everfeeds

import oauth.signpost.basic.DefaultOAuthProvider
import oauth.signpost.basic.DefaultOAuthConsumer

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import org.apache.log4j.Logger

/**
 * Created by alari @ 12.03.11 19:22
 */
class OAuthSession {
    private DefaultOAuthConsumer oauthConsumer
    private DefaultOAuthProvider oauthProvider
    private config

    private static ApplicationTagLib g = new ApplicationTagLib()
    private static log = Logger.getLogger(OAuthSession)

    OAuthSession(def config) {
        oauthConsumer = new DefaultOAuthConsumer(
                config.consumer.key, config.consumer.secret);

        this.config = config
    }

    String getAuthUrl(Map attrs) {
        attrs.absolute = true
        getProvider().retrieveRequestToken(
                oauthConsumer, g.createLink(attrs).toString());
    }

    void verify(String verifier) {
        getProvider().retrieveAccessToken(oauthConsumer, verifier);
    }

    String getToken() {
        oauthConsumer.getToken()
    }

    String getSecret() {
        oauthConsumer.getTokenSecret()
    }

    DefaultOAuthConsumer getConsumer() {
        oauthConsumer
    }

    DefaultOAuthProvider getProvider() {
        if(!oauthProvider) {
        oauthProvider = new DefaultOAuthProvider(
                config.requestTokenUrl,
                config.accessTokenUrl,
                config.authUrl);
        }
        oauthProvider
    }

    String apiGet(String url) {
        HttpURLConnection conn =
            (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10*1000);
        conn.setReadTimeout(25*1000);

        consumer.sign(conn);

        conn.connect();

        if(conn.getResponseCode() != 200) {
            log.error "Error: "+conn.getResponseCode()+" "+conn.getResponseMessage()
            return;
        }

        InputStream is = conn.getInputStream();

        BufferedReader br =
            new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String line;
        while (null != (line = br.readLine())) {
            buf.append(line).append("\n");
        }

        return buf.toString()
    }
}
