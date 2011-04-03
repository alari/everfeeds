package everfeeds.access

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import everfeeds.Access

/**
 * Created by alari @ 02.04.11 23:34
 */
class Manager {
    static private config = ConfigurationHolder.config.access

    static private Map<String,Map> configCache = [:]

    static private Map authCache = [:]

    static Map getConfig(String type){
        if(configCache[type]) {
            return configCache[type]
        }
        configCache[type] = config."${type}" as Map
        if(configCache[type].extend) {
            configCache[type] = getConfig(configCache[type].extend).merge(configCache[type])
        }
        return configCache[type]
    }

    static AAuth getAuth(String type){
        if(authCache[type]) {
            return authCache[type]
        }
        def authClass = getConfig(type)?.auth
        if(!authClass) return null
        authCache[type] = authClass.newInstance()
        return authCache[type]
    }

    static AAccessor getAccess(String type, Access access) {
        Class accessClass = getConfig(type)?.access
        if(!accessClass) return null
        accessClass.newInstance(access)
    }
}
