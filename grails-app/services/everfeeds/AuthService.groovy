package everfeeds

import org.springframework.web.context.request.RequestContextHolder
import everfeeds.access.Manager

class AuthService {

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    static transactional = "mongo"

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl(String accessType) {
        Manager.getAuth(accessType)?.getAuthUrl(session)
    }

    Access processCallback(String accessType, String verifierStr) {
        getAccess Manager.getAuth(accessType)?.authCallback(verifierStr, session)
    }

    Access getAccess(Map params) {
        if (!params.type || !params.id) {
            throw new IllegalArgumentException("Cannot get access without type/id")
        }

        String identity = params.type + ":" + params.id

        Access access = Access.findByAuthenticity(identity) ?: new Access(
                authenticity: identity,
                title: params.title,
                type: params.type
        )
        if (params.token) {
            access.token = params.token
            if (params.secret) access.secret = params.secret
            if (params.shard) access.shard = params.shard
            access.expired = false
        }
        access.save()
    }

    void setAccountRole(Account account, String authority) {
        authority = authority.toUpperCase()
        if (!authority.startsWith("ROLE_")) authority = "ROLE_" + authority
        AccountRole.create account, Role.getByAuthority(authority)
    }
}
