package everfeeds.access

/**
 * Created by alari @ 02.04.11 12:57
 */
abstract class Auth {
    abstract public String getAuthUrl(def session)

    private String typeCache

    public String getType(){
        if(!typeCache) {
            typeCache = this.class.package.name.tokenize(".").last()
        }
        typeCache
    }

    public Map getConfig(){
        Manager.getConfig(type)
    }
}
