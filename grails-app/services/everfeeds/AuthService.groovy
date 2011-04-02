package everfeeds

import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AuthService {

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()
    Map config = [:]

    static transactional = true

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    String getAuthUrl(String accessType) {
        if(!config."${accessType}") {
            buildAccessConfig(accessType)
            log.debug config
        }
        config."${accessType}".auth.getAuthUrl(config."${accessType}", accessType, session)
    }

    void buildAccessConfig(type) {
        log.debug "BEFORE BUILD: " +ConfigurationHolder.config.access."${type}"
        config."${type}" = ConfigurationHolder.config.access."${type}"
        if(config."${type}".extend) {
            String extend = ConfigurationHolder.config.access."${type}".extend
            if(!config."${extend}") {
                buildAccessConfig(extend)
            }
            config."$type" = config."$extend".merge(config."$type")
            log.debug "AFTER MERGE: ${config}"
        }
        log.debug "AFTER BUILD: " + config."$type"
    }

    Access processCallback(String accessType, String verifierStr) {
        if(!session."${accessType}") {
            log.error "No session object related to ${accessType}"
            return null
        }

        getAccess config."${accessType}".auth.authCallback(verifierStr, session)
    }

    Access getAccess(final String type, String screen, String token = null, String secret = null, String shard = null) {
        Access access = Access.findByIdentity(type + ":" + screen) ?: new Access(
                identity: type + ":" + screen,
                type: type
        )
        if (token) {
            access.token = token
            if (secret) access.secret = secret
            if (shard) access.shard = shard
            access.expired = false
        }
        access.save()
    }

    Access getAccess(Map params) {
        if (!params.type || !params.screen) {
            throw new IllegalArgumentException("Cannot get access without type/screen")
        }

        Access access = Access.findByIdentity(params.type + ":" + params.screen) ?: new Access(
                identity: params.type + ":" + params.screen,
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
