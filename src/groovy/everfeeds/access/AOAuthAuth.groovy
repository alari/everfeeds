package everfeeds.access

import org.scribe.model.Token
import org.scribe.model.Verifier
import org.scribe.oauth.OAuthService
import everfeeds.OAuthHelper

/**
 * Created by alari @ 02.04.11 13:01
 */
abstract class AOAuthAuth extends AAuth {

    private static final Token EMPTY_TOKEN = null;

    abstract public Map authCallback(String verifierStr, Object session)

    protected Map authCallbackHelper(String verifierStr, Object session, Closure closure) {
        Verifier verifier = new Verifier(verifierStr);
        Token accessToken = session?."${type}"?.service?.getAccessToken(session?."${type}"?.token ?: EMPTY_TOKEN, verifier);
        session."${type}" = null

        Map params = closure.call(accessToken)
        if (!params?.screen) {
            return null
        }
        params.type = type
        params.token = accessToken.token
        params.secret = accessToken.secret

        params
    }

    public String getAuthUrl(Object session) {
        OAuthService service = OAuthHelper.getOAuthService(config.oauth, type)

        Token requestToken = EMPTY_TOKEN;
        if(service instanceof org.scribe.oauth.OAuth10aServiceImpl) {
            requestToken = service.getRequestToken();
        }
        session."${type}" = [service: service, token: requestToken]

        service.getAuthorizationUrl requestToken
    }
}