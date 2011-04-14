package everfeeds.access

import everfeeds.Access
import everfeeds.Category
import everfeeds.Tag
import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.TagEnvelop
import everfeeds.envelops.EntryFace

/**
 * Created by alari @ 14.03.11 15:00
 */
abstract class Accessor {
    protected Access access

    private String typeCache

    public Map getConfig(){
        Manager.getConfig(type)
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
            tag = actualTags.find {it.authenticity == t.authenticity}
            if (!tag) {
                access.removeFromTags t
                t.delete()
            } else {
                if(t.titleIsCode) return;
                t.title = tag.title
                t.save()
                actualTags.remove(tag)
            }
        }
        actualTags.each { t ->
            access.addToTags new Tag(authenticity: t.authenticity, title: t.title, access: access, titleIsCode: t.titleIsCode).save()
        }

        currentCategories.each { c ->
            category = actualCategories.find {it.authenticity == c.authenticity}
            if (!category) {
                access.removeFromCategories c
                c.delete()
            } else {
                if(c.titleIsCode) return;
                c.title = category.title
                c.save()
                actualCategories.remove(category)
            }
        }
        actualCategories.each { c ->
            access.addToCategories new Category(authenticity: c.authenticity, title: c.title, access: access, titleIsCode: c.titleIsCode).save()
        }

        access.lastSync = new Date()
        access.save()
    }

    public String getType(){
        if(!typeCache) {
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
