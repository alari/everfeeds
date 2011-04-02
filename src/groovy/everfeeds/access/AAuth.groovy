package everfeeds.access

/**
 * Created by alari @ 02.04.11 12:57
 */
abstract class AAuth {
    abstract static public String getAuthUrl(def config, String accessType, Object session)

    abstract static public String getType()
}
