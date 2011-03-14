package everfeeds

import com.evernote.edam.type.User
import com.evernote.edam.userstore.UserStore
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.springframework.web.context.request.RequestContextHolder

class EvernoteAuthService {

    static transactional = true

    def grailsApplication

    def springSecurityService
    def evernoteService

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    String getAuthUrl() {
        session.evernote = new OAuthSession(grailsApplication.config.evernote)
        session.evernote.provider.setRequestHeader("User-Agent", grailsApplication.config.evernote.userAgent)
        session.evernote.getAuthUrl(controller: "access", action: "evernoteCallback")
    }

    Access processCallback(String verifier) {
        if (!session.evernote) {
            log.error "no session.evernote"
            return null
        }

        session.evernote.verify(verifier)
        String accessToken = session.evernote.token;
        session.evernote = null

        // Getting UserStore
        THttpClient userStoreTrans = new THttpClient("https://" + grailsApplication.config.evernote.host + "/edam/user");
        userStoreTrans.setCustomHeader("User-Agent", grailsApplication.config.evernote.userAgent);
        TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
        UserStore.Client userStore = new UserStore.Client(userStoreProt, userStoreProt);

        // Finding access by username
        User user = userStore.getUser(accessToken)

        Access.findByIdentity(Access.TYPE_EVERNOTE + ":" + user.username) ?: new Access(
                identity: Access.TYPE_EVERNOTE + ":" + user.username,
                type: Access.TYPE_EVERNOTE,
                token: accessToken,
                shard: user.shardId).save(flush: true)
    }

    def setAccountRole(Account account) {
        AccountRole.create account, Role.findByAuthority("EVERNOTE") ?: new Role(authority: "EVERNOTE").save()
    }
}
