package everfeeds.access

import everfeeds.Access
import everfeeds.Category
import everfeeds.Tag
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 14.03.11 15:00
 */
abstract class Accessor {
  protected Access access

  private String typeCache

  private Parser parserCache

  public Access getAccess() {
    access
  }

  public Map getConfig() {
    Manager.getConfig(type)
  }

  public Parser getParser() {
    if (!parserCache) {
      parserCache = Manager.classForSuffix(type, "Parser").newInstance()
      parserCache.accessor = this
    }
    parserCache
  }

  public void sync() {
    List<TagEnvelop> actualTags = tags
    List<CategoryEnvelop> actualCategories = categories

    Set<Tag> currentTags = access.tags
    Set<Category> currentCategories = access.categories

    if (access.expired) return;

    TagEnvelop tag
    CategoryEnvelop category

    currentTags.each { t ->
      tag = actualTags.find {it.identity == t.identity}
      if (!tag) {
        access.removeFromTags t
        t.delete()
      } else {
        if (t.titleIsCode) return;
        t.title = tag.title
        t.save()
        actualTags.remove(tag)
      }
    }
    actualTags.each { t ->
      access.addToTags new Tag(identity: t.identity, title: t.title, access: access, titleIsCode: t.titleIsCode).save()
    }

    currentCategories.each { c ->
      category = actualCategories.find {it.identity == c.identity}
      if (!category) {
        access.removeFromCategories c
        c.delete()
      } else {
        if (c.titleIsCode) return;
        c.title = category.title
        c.save()
        actualCategories.remove(category)
      }
    }
    actualCategories.each { c ->
      access.addToCategories new Category(identity: c.identity, title: c.title, access: access, titleIsCode: c.titleIsCode).save()
    }

    access.lastSync = new Date()
    access.save()
  }

  public String getType() {
    if (!typeCache) {
      typeCache = this.class.package.name.tokenize(".").last()
    }
    typeCache
  }

  abstract public boolean isPullable()

  abstract public boolean isPushable()

  abstract public List<TagEnvelop> getTags()

  abstract public List<CategoryEnvelop> getCategories()

  abstract public List<EntryEnvelop> pull(Map params = [:])

  abstract public void push(EntryFace entry)
}
