package everfeeds;

import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;

/**
 * Created by alari @ 01.04.11 22:15
 */
public class EvernoteSandboxApi extends EvernoteApi.Sandbox {
    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format("https://sandbox.evernote.com/OAuth.action?oauth_token=%s", requestToken.getToken());
    }
}
