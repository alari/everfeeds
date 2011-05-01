package everfeeds

import everfeeds.access.Manager
import everfeeds.envelops.EntryFace
import com.mongodb.BasicDBObject

class Entry implements EntryFace {

  String id

  static mapWith = "mongo"

  String identity
  String title
  String kind
  String imageUrl

  String description
  String content

  String author
  String authorIdentity
  boolean accessIsAuthor

  boolean isPublic

  String sourceUrl
  Date placedDate

  long categoryId
  long accountId
  long accessId
  List tagIds

  Date dateCreated
  Date lastUpdated

  String type

  static transients = ["tagIdentities", "categoryIdentity", "kindClass", "access", "account", "category", "tags"]

  List<Tag> getTags(){
    Tag.findAllByIdInList(tagIds)
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
    placedDate index: true
    dateCreated index: true
    description maxSize: 2048
    content maxSize: 1024 * 1024
    author nullable: true
    authorIdentity nullable: true
    accessIsAuthor nullable: true
    sourceUrl nullable: true
    imageUrl nullable: true
  }

  static mapping = {
    compoundIndex tagIds: 1, accessId:1, dateCreated:-1, categoryId:1
    compoundIndex accountId: 1, dateCreated:-1
    cimpoundIndex authorIdentity: 1, type: 1
    //compoundIndex accessId:1, identity: 1, kind: 1, unique:true, dropDups:true
  }

  static namedQueries = {
    findAllFiltered { params ->
      and {
        eq("accessId", params.access.id)
        "${params.getNew ? 'gt' : 'lt'}"("dateCreated", params.splitDate)
        // Handle categories
        if (params.withCategories?.size()) {
          'in'("categoryId", params.withCategories.id)
        } else if (params.withoutCategories?.size()) {
          not {
            'in'("categoryId", params.withoutCategories.id)
          }
        }
        // Handle tags
        Map tagsQuery = [:]
        if (params.withTags?.size()) {
          tagsQuery.'$all' = params.withTags.id
        }
        if (params.withoutTags?.size()) {
          tagsQuery.'$nin' = params.withoutTags.id
        }
        if(tagsQuery.size()) {
          eq("tagIds", tagsQuery)
        }
        // Handle kinds
        if(params.withKinds?.size()) {
          eq("kind", ['$in':params.withKinds])
        } else if(params.withoutKinds?.size()) {
          eq("kind", ['$nin':params.withoutKinds])
        }
      }
    }

    findAllFilteredByAccount { params ->
      and {
        eq("accountId", params.account.id)
        "${params.getNew ? 'gt' : 'lt'}"("dateCreated", params.splitDate)
      }
    }
  }

  static boolean isUnique(Access access, String identity, String kind) {
    createCriteria().count {
      and {
        eq "accessId", access.id
        eq "identity", identity
        eq "kind", kind
      }
    } == 0
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
