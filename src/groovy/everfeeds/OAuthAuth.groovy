package everfeeds

import everfeeds.thrift.util.Type
import grails.converters.deep.JSON
import grails.converters.deep.XML
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import org.scribe.builder.ServiceBuilder
import org.scribe.oauth.OAuthService
import org.scribe.model.*
import everfeeds.AccessInfo

/**
 * Created by alari @ 02.04.11 13:01
 */
abstract class OAuthAuth {

  static ApplicationTagLib g = new ApplicationTagLib()

  private static final Token EMPTY_TOKEN = null;

  private String key
  private String secret
  private Class apiClass
  private String scope
  private Type type

  private OAuthService service
  private Token requestToken = EMPTY_TOKEN

  protected void key(String k) {key = k}

  protected void secret(String s) {secret = s}

  protected void provider(Class apiCls) {apiClass = apiCls}

  protected void scope(String sc) {scope = sc}

  protected void type(Type tp) {type = tp}

  protected Logger getLog() {
    Logger.getLogger(this.class)
  }

  protected OAuthService getService() {
    if (service) return service

    ServiceBuilder builder = new ServiceBuilder()
    builder.provider(apiClass)
    builder.apiKey(key)
    builder.apiSecret(secret)
    if (type) {
      builder.callback(g.createLink(
          controller: "access",
          action: "callback",
          id: type.toString(),
          absolute: true
      ).toString())
    }
    if (scope) {
      builder.scope(scope)
    }

    service = builder.build()
    service
  }

  abstract protected AccessInfo getAccessInfo(Token accessToken)

  public AccessInfo authCallback(String verifierStr) {
    Verifier verifier = new Verifier(verifierStr)
    Token accessToken = getService().getAccessToken(requestToken, verifier)
    AccessInfo info = getAccessInfo(accessToken)
    if(!info) return info
    info.accessToken = accessToken.token
    info.accessSecret = accessToken.secret
    if(!info.title) info.title = info.identity
    info.type = type
    info
  }

  public String getAuthUrl() {
    getService()

    if (service instanceof org.scribe.oauth.OAuth10aServiceImpl) {
      requestToken = service.getRequestToken();
    }

    service.getAuthorizationUrl requestToken
  }

  protected callApiJson(String url, Token accessToken) {
    JSON.parse callApi(url, accessToken)
  }

  protected callApiXml(String url, Token accessToken) {
    XML.parse callApi(url, accessToken)
  }

  protected String callApi(String url, Token accessToken) {
    OAuthRequest request = new OAuthRequest(Verb.GET, url);
    service.signRequest(accessToken, request);

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
}