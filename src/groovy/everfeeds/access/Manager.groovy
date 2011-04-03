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

    static private Map accessorClassCache = [:]

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

    static Map getConfigs(){
        // TODO: return full configCache instead
        config
    }

    static AAuth getAuth(String type){
        if(authCache[type]) {
            return authCache[type]
        }
        if(getConfig(type)?.auth == false) return null
        authCache[type] = classForSuffix(type, "Auth").newInstance()
        return authCache[type]
    }

    static AAccessor getAccessor(String type, Access access) {
        if(!accessorClassCache[type]) {
            Class accessClass = classForSuffix(type, "Accessor")
            if(!accessClass) return null
            accessorClassCache[type] = accessClass
        }
        accessorClassCache[type].newInstance(access)
    }

    static Class classForSuffix(String type, String suffix){
        Class.forName(this.package.name+"."+type+"."+type.capitalize()+suffix, true, Thread.currentThread().contextClassLoader)
    }
}
