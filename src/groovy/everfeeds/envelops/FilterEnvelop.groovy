package everfeeds.envelops

import everfeeds.*

/**
 * Created by alari @ 08.04.11 13:14
 */
class FilterEnvelop implements FilterFace {
  Access access
  Tag[] withTags = []
  Tag[] withoutTags = []
  Category[] withCategories = []
  Category[] withoutCategories = []
  Account account

  Date splitDate
  boolean getNew

  List<Entry> findEntries(Map listParams = [:]) {
    if (!listParams.containsKey("sort")) listParams["sort"] = "placedDate"
    if (!listParams.containsKey("order")) listParams["order"] = "desc"

    def criteria

    if (access) {
      Entry.findAllFiltered(this).list(listParams)
    } else {
      def c = Entry.createCriteria()
      c.list(listParams) {
        and {
          eq "accountId", account.id
          "${getNew ? 'gt' : 'lt'}"("dateCreated", splitDate)
        }
      }
    }
  }
}
