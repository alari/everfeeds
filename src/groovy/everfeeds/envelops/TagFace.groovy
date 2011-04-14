package everfeeds.envelops

/**
 * Created by alari @ 18.03.11 17:51
 */
public interface TagFace {
    String getAuthenticity()

    void setAuthenticity(String s)

    String getTitle()

    void setTitle(String s)

    void setTitleIsCode(boolean b)

    boolean getTitleIsCode()
}