package everfeeds

import everfeeds.thrift.util.Type
import everfeeds.annotation.Accessor

/**
 * @author Dmitry Kurinskiy
 * @since 27.06.11 20:17
 */
class Auth {
  static private Map<Type,Class> auths = [:]

  static {
    Package.getClasses("everfeeds.auth").each {Class cls->
      if(!cls.getAnnotation(Accessor)) return;
      auths.put(((Accessor)cls.getAnnotation(Accessor)).value(), cls)
    }
  }

  static public List<Type> getTypes(){
    auths.keySet().asList()
  }

  static public OAuthAuth getInstance(Type type){
    auths.get(type).newInstance()
  }
}