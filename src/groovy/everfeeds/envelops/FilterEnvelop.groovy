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
  String[] withKinds = []
  String[] withoutKinds = []
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
      Entry.findAllFilteredByAccount(this).list(listParams)
    }
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

    if (params?.getNew) {
      getNew = true
      splitDate = new Date(params.long("newTime") ?: (params.long("listTime") ?: System.currentTimeMillis()))
    } else {
      splitDate = new Date(params.listTime ? params.long("listTime") : System.currentTimeMillis())
    }
  }

  private List filterParam(what, param, params) {
    params.list(param + "[]").collect {p -> access."${what}".find {i -> i.id == Long.parseLong(p.toString())}}
  }

  String asJavascript(){
    """{
        ${access ? 'access:'+access.id+",":''}
        wtag: ${withTags*.id.encodeAsJavaScript()},
        wotag: ${withoutTags*.id.encodeAsJavaScript()},
        wcat: ${withCategories*.id.encodeAsJavaScript()},
        wocat: ${withoutCategories*.id.encodeAsJavaScript()},
        wkind: ${withKinds.encodeAsJavaScript()},
        wokind: ${withoutKinds.encodeAsJavaScript()},
        ${getNew?"newTime:${System.currentTimeMillis()},":""}
        listTime: ${splitDate.time},
    }"""
  }
}
