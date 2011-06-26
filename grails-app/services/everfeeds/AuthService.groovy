package everfeeds

import everfeeds.auth.OAuthAuth
import everfeeds.thrift.util.Type
import everfeeds.auth.GmailAuth
import everfeeds.auth.EvernoteAuth
import everfeeds.auth.FacebookAuth
import everfeeds.auth.GreaderAuth
import everfeeds.auth.LinkedInAuth
import everfeeds.auth.TwitterAuth
import everfeeds.auth.VkontakteAuth
import javax.servlet.http.HttpSession
import everfeeds.auth.AccessInfo

class AuthService {
    static transactional = true

    String getAuthUrl(String accessType, HttpSession session) {
      OAuthAuth auth = getOAuthAuth(accessType)
      String url = auth.getAuthUrl()
      session.setAttribute(accessType, auth)
      url
    }

  AccessInfo processCallback(String accessType, String verifierStr, HttpSession session) {
    OAuthAuth auth = (OAuthAuth)session.getAttribute(accessType)
    if(!auth) throw new Exception("Authentication session was broken")

    AccessInfo accessInfo = auth.authCallback(verifierStr)
    if(!accessInfo || !accessInfo.identity) throw new Exception("Authentication failed")

    session.removeAttribute(accessType)

    accessInfo
  }

  private OAuthAuth getOAuthAuth(String type){
    switch(Type.getByName(type)){
      case Type.GMAIL: return new GmailAuth()
      case Type.EVERNOTE: return new EvernoteAuth()
      case Type.FACEBOOK: return new FacebookAuth()
      case Type.GREADER: return new GreaderAuth()
      case Type.LINKEDIN: return new LinkedInAuth()
      case Type.TWITTER: return new TwitterAuth()
      case Type.VKONTAKTE: return new VkontakteAuth()
    }
  }
}
