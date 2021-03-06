package everfeeds

import everfeeds.envelops.FilterFace
import everfeeds.envelops.FilterHelper

class Filter implements FilterFace {

  static mapWith = "mongo"

  String id

  String title

  List withTagIds = []
  List withoutTagIds = []
  List categoryIds = []
  boolean categoryWith = true

  String[] withKinds = []
  String[] withoutKinds = []

  long accountId
  long accessId

  Date splitDate
  boolean getNew

  Date dateCreated
  Date lastUpdated

  static transients = ["getNew", "splitDate", "access", "account", "withTags", "withoutTags", "withCategories", "withoutCategories"]

  static constraints = {
  }

  List<Entry> findEntries(Map listParams = [:]) {
    FilterHelper.findEntries this, listParams
  }

  Account getAccount() {
    Account.get(accountId)
  }

  Access getAccess() {
    Access.get(accessId)
  }

  Tag[] getWithTags() {
    Tag.findAllByIdInList(withTagIds)
  }

  Tag[] getWithoutTags() {
    Tag.findAllByIdInList(withoutTagIds)
  }

  Category[] getWithCategories() {
    Category.findAllByIdInList(categoryWith ? categoryIds : [])
  }

  Category[] getWithoutCategories() {
    Category.findAllByIdInList(categoryWith ? [] : categoryIds)
  }

  String asJavascript() {
    FilterHelper.asJavascript this
  }

  void setAccess(Access access) {
    accessId = access.id
  }

  void setAccount(Account account) {
    accountId = account.id
  }

  void setWithTags(Tag[] tags) {
    withTagIds = tags*.id
  }

  void setWithoutTags(Tag[] tags) {
    withoutTagIds = tags*.id
  }

  void setWithCategories(Category[] categories) {
    if (categories.size()) {
      categoryIds = categories*.id
      categoryWith = true
    }
  }

  void setWithoutCategories(Category[] categories) {
    if (categories.size()) {
      categoryIds = categories*.id
      categoryWith = false
    }
  }
}
