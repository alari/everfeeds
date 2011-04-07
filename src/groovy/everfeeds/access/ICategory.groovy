package everfeeds.access

/**
 * Created by alari @ 18.03.11 17:50
 */
public interface ICategory {
    String getIdentity()

    void setIdentity(String s)

    String getTitle()

    void setTitle(String s)

    void setTitleIsCode(boolean b)

    boolean getTitleIsCode()
}