package everfeeds.envelops

import everfeeds.Access
import everfeeds.Category
import everfeeds.Tag

/**
 * Created by alari @ 08.04.11 13:07
 */
public interface FilterFace {
  public Access getAccess()

  //public NamedCriteriaProxy findEntries()

  public Tag[] getWithTags()

  public Tag[] getWithoutTags()

  public Category[] getWithCategories()

  public Category[] getWithoutCategories()
}