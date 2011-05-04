package everfeeds.envelops

import everfeeds.*

/**
 * @author Dmitry Kurinskiy
 * @since 08.04.11 13:14
 */
class FilterEnvelop implements FilterFace {
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

  static List<Entry> findEntriesHelper(FilterFace filter, Map listParams) {
    if (!listParams.containsKey("sort")) listParams["sort"] = "placedDate"
    if (!listParams.containsKey("order")) listParams["order"] = "desc"

    def criteria

    if (filter.access) {
      Entry.findAllFiltered(filter).list(listParams)
    } else {
      Entry.findAllFilteredByAccount(filter).list(listParams)
    }
  }

  List<Entry> findEntries(Map listParams = [:]) {
    findEntriesHelper this, listParams
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
    filter
  }

  private List filterParam(what, param, params) {
    params.list(param + "[]").collect {p -> access."${what}".find {i -> i.id == Long.parseLong(p.toString())}}
  }

  static String asJavascriptHelper(FilterFace filter) {
    """{
        ${filter.access ? 'access:' + filter.access.id + "," : ''}
        wtag: ${filter.withTags*.id.encodeAsJavaScript()},
        wotag: ${filter.withoutTags*.id.encodeAsJavaScript()},
        wcat: ${filter.withCategories*.id.encodeAsJavaScript()},
        wocat: ${filter.withoutCategories*.id.encodeAsJavaScript()},
        wkind: ${filter.withKinds.encodeAsJavaScript()},
        wokind: ${filter.withoutKinds.encodeAsJavaScript()},
        ${filter.getNew ? "newTime:${System.currentTimeMillis()}," : ""}
        listTime: ${filter.splitDate.time},
    }"""
  }

  String asJavascript() {
    asJavascriptHelper this
  }
}
