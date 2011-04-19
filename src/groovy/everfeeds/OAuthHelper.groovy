package everfeeds

import grails.converters.deep.JSON
import grails.converters.deep.XML
import org.scribe.builder.ServiceBuilder
import org.scribe.model.OAuthRequest
import org.scribe.model.Response
import org.scribe.model.Token
import org.scribe.model.Verb
import org.scribe.oauth.OAuthService

/**
 * Created by alari @ 03.04.11 13:13
 */
class OAuthHelper {
  static g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

  static public String callApi(def oauthConfig, String url, String token, String secret, Verb method = Verb.GET) {
    OAuthService service = getOAuthService(oauthConfig)
    OAuthRequest request = new OAuthRequest(method, url);
    def tkn = new Token(token, secret)
    service.signRequest(tkn, request);
    final result = request.send()
    if (result != null) {
      if (result.body != null) {
        return result?.body;
      } else if (((Response) result).headers.get("Content-Type").toLowerCase().contains("text/javascript")) {
        Response response = ((Response) result)
        return response.getStream().getText("UTF-8") // TODO(low): other encodings
      }
    }
    return ""
  }

  static public Object callJsonApi(def oauthConfig, String url, String token, String secret, Verb method = Verb.GET) {
    JSON.parse(callApi(oauthConfig, url, token, secret, method))
  }

  static public Object callXmlApi(def oauthConfig, String url, String token, String secret, Verb method = Verb.GET) {
    XML.parse(callApi(oauthConfig, url, token, secret, method))
  }

  static public OAuthService getOAuthService(def oauthConfig, String accessType = null) {
    ServiceBuilder builder = new ServiceBuilder()
    builder.provider(oauthConfig.provider)
    builder.apiKey(oauthConfig.key.toString())
    builder.apiSecret(oauthConfig.secret.toString())
    if (accessType) {
      final link = g.createLink(
          controller: "access",
          action: "callback",
          id: accessType,
          absolute: true
      ).toString()
      builder.callback(link)
    }
    if (oauthConfig.scope) {
      builder.scope(oauthConfig.scope.toString())
    }

    builder.build()
  }
}
