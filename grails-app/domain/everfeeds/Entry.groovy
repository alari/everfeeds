package everfeeds

import everfeeds.access.Manager
import everfeeds.envelops.EntryFace
import org.hibernate.FetchMode
import org.hibernate.transform.DistinctRootEntityResultTransformer

class Entry implements EntryFace {

  String id

  static mapWith = "mongo"

  String identity
  String title
  String kind
  String imageUrl
  String content
  String author
  String sourceUrl
  Date placedDate

  //Category category
  //Account account
  //Access access

  long categoryId
  long accountId
  long accessId
  List tagIds

  Date dateCreated
  Date lastUpdated

  String type

  //static hasMany = [tags: Tag]

  //static belongsTo = [Access, Account, Category, Tag]

  static transients = ["tagIdentities", "categoryIdentity", "kindClass", "access", "account", "category", "tags"]

  List<Tag> getTags(){
    Tag.findAllByIdIn(tagIds)
  }

  Access getAccess(){
    Access.get(accessId)
  }

  void setAccess(Access access) {
    accessId = access.id
  }

  Account getAccount(){
    Account.get(accountId)
  }

  void setAccount(Account account) {
    accountId = account.id
  }

  Category getCategory(){
    Category.get(categoryId)
  }

  void setCategory(Category category){
    categoryId = category.id
  }

  static constraints = {
    //placedDate index: "placedDateIndex"
    //dateCreated index: "dateCreatedIndex"
    content maxSize: 1024 * 1024
    author nullable: true
    sourceUrl nullable: true
    imageUrl nullable: true
  }

  static namedQueries = {
    findAllFiltered { params ->
      and {
        eq("accessId", params.access.id)
        "${params.getNew ? 'gt' : 'lt'}"("dateCreated", params.splitDate)
        if (params.withCategories?.size()) {
          'in'("categoryId", params.withCategories.id)
        }
        if (params.withoutCategories?.size()) {
          not {
            'in'("categoryId", params.withoutCategories.id)
          }
        }
        if (params.withTags?.size()) {
          eq("tagIds", params.withTags.id)
        }
        if (params.withoutTags?.size()) {

            not {
              eq("tagIds", params.withoutTags.id)
            }
        }
      }
      //fetchMode("tags", FetchMode.EAGER)
      //fetchMode("content", FetchMode.LAZY)
      //resultTransformer(new DistinctRootEntityResultTransformer())
      order "placedDate", "desc"
    }
  }

  List<String> getTagIdentities() {
    tags*.identity
  }

  void setTagIdentities(List<String> identities) {
    tagIds = access.tags.findAll{it.identity in identities}*.id
  }

  String getCategoryIdentity() {
    category.identity
  }

  void setCategoryIdentity(String identity) {
    categoryId = access.categories.find {it.identity == identity}.id
  }

  Class getKindClass() {
    Manager.classForKind(type, kind)
  }
}
