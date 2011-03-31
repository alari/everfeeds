package everfeeds.manager

/**
 * Created by alari @ 18.03.11 17:51
 */
interface IEntry {
    String getIdentity()

    void setIdentity(String s)

    String getTitle()

    void setTitle(String s)

    String getImageUrl()

    void setImageUrl(String s)

    String getContent()

    void setContent(String s)

    String getAuthor()

    void setAuthor(String s)

    String getSourceUrl()

    void setSourceUrl(String s)

    Date placedDate

    List<String> getTagIdentities()

    String getCategoryIdentity()
}
