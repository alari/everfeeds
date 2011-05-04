package everfeeds.envelops

import everfeeds.*

/**
 * @author Dmitry Kurinskiy
 * @since 08.04.11 13:14
 */
class FilterEnvelop implements FilterFace {
  String title

  Access access
  Tag[] withTags = []
  Tag[] withoutTags = []
  Category[] withCategories = []
  Category[] withoutCategories = []
  String[] withKinds = []
  String[] withoutKinds = []
  Account account

  Filter filter

  Date splitDate
  boolean getNew



  List<Entry> findEntries(Map listParams = [:]) {
    FilterHelper.findEntries this, listParams
  }

  void buildFromParams(params, Access access) {
    if (access) {
      this.access = access
      withTags = filterParam("tags", "wtag", params)
      withoutTags = filterParam("tags", "wotag", params)

      withCategories = filterParam("categories", "wcat", params)
      withoutCategories = filterParam("categories", "wocat", params)

      withKinds = params.list("wkind[]")
      withoutKinds = params.list("wokind[]")
    }

    if (params.filter) {
      filter = Filter.get(params.filter)
    }

    if (params?.getNew) {
      getNew = true
      splitDate = new Date(params.long("newTime") ?: (params.long("listTime") ?: System.currentTimeMillis()))
    } else {
      splitDate = new Date(params.listTime ? params.long("listTime") : System.currentTimeMillis())
    }
  }

  Filter store() {
    if (!filter) {
      filter = new Filter()
      filter.access = access
      filter.account = account
    }

    ["withKinds", "withoutKinds",
        "withCategories", "withoutCategories",
        "withTags", "withoutTags"
    ].each {
      filter."${it}" = this."${it}"
    }

    if(title) {
      filter.title = title
    }
    filter.save()
  }

  private List filterParam(what, param, params) {
    params.list(param + "[]").collect {p -> access."${what}".find {i -> i.id == Long.parseLong(p.toString())}}
  }



  String asJavascript() {
    FilterHelper.asJavascript this
  }
}
