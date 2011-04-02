package everfeeds.access

import everfeeds.Access
import everfeeds.Category
import everfeeds.Tag
import everfeeds.access.envelops.CategoryEnvelop
import everfeeds.access.envelops.EntryEnvelop
import everfeeds.access.envelops.TagEnvelop

/**
 * Created by alari @ 14.03.11 15:00
 */
abstract class AAccess {
    protected Access access
    static final int NUM = 150

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
                t.title = tag.title
                t.save()
                actualTags.remove(tag)
            }
        }
        actualTags.each { t ->
            access.addToTags new Tag(identity: t.identity, title: t.title, access: access).save()
        }

        currentCategories.each { c ->
            category = actualCategories.find {it.identity == c.identity}
            if (!category) {
                access.removeFromCategories c
                c.delete()
            } else {
                c.title = category.title
                c.save()
                actualCategories.remove(category)
            }
        }
        actualCategories.each { c ->
            access.addToCategories new Category(identity: c.identity, title: c.title, access: access).save()
        }

        access.lastSync = new Date()
        access.save()
    }

    abstract public boolean isPullable()

    abstract public boolean isPushable()

    abstract public List<TagEnvelop> getTags()

    abstract public List<CategoryEnvelop> getCategories()

    abstract public List<EntryEnvelop> pull(Map params = [:])

    abstract public void push(IEntry entry)
}
