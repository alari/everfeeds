package everfeeds

import everfeeds.envelops.EntryFace
import org.hibernate.transform.DistinctRootEntityResultTransformer
import org.hibernate.FetchMode
import everfeeds.access.Kind
import everfeeds.access.Manager

class Entry implements EntryFace {

    String identity
    String title
    String kind
    String imageUrl
    String content
    String author
    String sourceUrl
    Date placedDate

    Category category
    Account account
    Access access

    Date dateCreated
    Date lastUpdated

    private String type

    static hasMany = [tags: Tag]

    static belongsTo = [Access, Account, Category, Tag]

    static transients = ["tagIdentities", "categoryIdentity", "type", "kindClass"]

    static constraints = {
        placedDate index: "placedDateIndex"
        dateCreated index: "dateCreatedIndex"
        content maxSize: 1024 * 1024
        author nullable: true
        sourceUrl nullable: true
        imageUrl nullable: true
    }

    static namedQueries = {
        findAllFiltered { params ->
            and {
                eq("access", params.access)
                "${params.getNew ? 'gt' : 'lt'}"("dateCreated", params.splitDate)
                if(params.withCategories?.size()) {
                    'in'("category", params.withCategories)
                }
                if(params.withoutCategories?.size()) {
                    not{
                        'in'("category", params.withoutCategories)
                    }
                }
                if (params.withTags?.size()) {
                    tags {
                        'in'("id", params.withTags.id)
                    }
                }
                if(params.withoutTags?.size()) {
                    tags {
                        not {
                            'in'("id", params.withoutTags.id)
                        }
                    }
                }
            }
            fetchMode("tags", FetchMode.EAGER)
            fetchMode("content", FetchMode.LAZY)
            resultTransformer(new DistinctRootEntityResultTransformer())
            order "placedDate", "desc"
        }
    }

    List<String> getTagIdentities() {
        tags*.identity
    }

    String getCategoryIdentity() {
        category.identity
    }

    String getType() {
        if (!type) type = access.type
        type
    }

  Class getKindClass(){
    Manager.classForKind(type, kind)
  }
}
