package everfeeds.access

import everfeeds.Access
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Created by alari @ 02.04.11 23:34
 */
class Manager {
  static public final int MAX_NUM = 500
  static public final int NUM = 100

  static private config = ConfigurationHolder.config.access

  static private Map<String, Map> configCache = [:]

  static private Map authCache = [:]

  static private Map accessorClassCache = [:]

  static Map getConfig(String type) {
    if (configCache.containsKey(type)) {
      return configCache[type]
    }
    configCache[type] = config."${type}" as Map
    if (configCache[type].containsKey("extend")) {
      configCache[type] = mergeRecursive(getConfig(configCache[type].extend), configCache[type])
    }
    return configCache[type]
  }

  // TODO: add it to Map metaclass

  static private Map mergeRecursive(Map base, Map over) {
    Map result = [:]
    base.each {k, v ->
      // If current value is a map, we should replace or merge it
      if (v instanceof Map && over.containsKey(k) && over[k] instanceof Map) {
        result[k] = mergeRecursive(v, over.remove(k) as Map);
      } else {
        result[k] = over.containsKey(k) ? over.remove(k) : v
      }
    }
    over.each {k, v ->
      result[k] = v
    }
    result
  }

  static Map getConfigs() {
    // TODO: improve performance
    config.each {k, v -> getConfig(k)}
    configCache
  }

  static Auth getAuth(String type) {
    if (authCache.containsKey(type)) {
      return authCache[type]
    }
    if (getConfig(type)?.auth == false) return null
    try {
      authCache[type] = classForSuffix(type, "Auth").newInstance()
    } catch (ClassNotFoundException e) {
      authCache[type] = null
    }
    return authCache[type]
  }

  static Accessor getAccessor(String type, Access access) {
    if (!accessorClassCache[type]) {
      Class accessClass = classForSuffix(type, "Accessor")
      if (!accessClass) return null
      accessorClassCache[type] = accessClass
    }
    accessorClassCache[type].newInstance(access)
  }

  static Class classForSuffix(String type, String suffix) {
    Class.forName(this.package.name + "." + type + "." + type.capitalize() + suffix.capitalize(), true, Thread.currentThread().contextClassLoader)
  }

  static Class classForKind(String type, String kind) {
    Class.forName(this.package.name + "." + type + ".kind." + type.capitalize() + kind.capitalize(), true, Thread.currentThread().contextClassLoader)
  }
}
