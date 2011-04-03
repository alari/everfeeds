package everfeeds.access

/**
 * Created by alari @ 02.04.11 12:57
 */
abstract class AAuth {
    abstract public String getAuthUrl(def session)

    public String getType(){
        this.class.package.name.tokenize(".").last()
    }

    public Map getConfig(){
        Manager.getConfig(type)
    }
}
