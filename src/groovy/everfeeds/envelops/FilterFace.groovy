package everfeeds.envelops

import everfeeds.Access
import everfeeds.Category
import everfeeds.Tag
import everfeeds.Account

/**
 * @author Dmitry Kurinskiy
 * @since 08.04.11 13:07
 */
public interface FilterFace {
  public Access getAccess()
  public void setAccess(Access a)

  public Account getAccount()
  public void setAccount(Account a)

  public Tag[] getWithTags()
  public void setWithTags(Tag[] tags)

  public Tag[] getWithoutTags()
  public void setWithoutTags(Tag[] tags)

  public Category[] getWithCategories()
  public void setWithCategories(Category[] categories)

  public Category[] getWithoutCategories()
  public void setWithoutCategories(Category[] categories)

  String[] getWithKinds()
  void setWithKinds(String[] kinds)

  String[] getWithoutKinds()
  void setWithoutKinds(String[] kinds)

  Date getSplitDate()
  void setSplitDate(Date d)

  boolean getGetNew()
  void setGetNew(boolean b)


  List<EntryFace> findEntries(Map listParams)
  String asJavascript()
}