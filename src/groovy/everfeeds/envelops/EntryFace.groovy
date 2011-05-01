package everfeeds.envelops

/**
 * Created by alari @ 18.03.11 17:51
 */
interface EntryFace {
  String getIdentity()

  void setIdentity(String s)

  String getTitle()

  void setTitle(String s)

  String getKind()

  void setKind(String s)

  String getImageUrl()

  void setImageUrl(String s)

  String getContent()

  void setContent(String s)

  String getAuthor()

  void setAuthor(String s)

  String getAuthorIdentity()

  void setAuthorIdentity(String s)

  boolean getAccessIsAuthor()

  void setAccessIsAuthor(boolean b)

  boolean getIsPublic()

  void setIsPublic(boolean b)

  String getSourceUrl()

  void setSourceUrl(String s)

  Date placedDate

  List<String> getTagIdentities()

  void setTagIdentities(List<String> identities)

  String getCategoryIdentity()

  void setCategoryIdentity(String s)

  String getType()
}
