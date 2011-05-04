package everfeeds.envelops

import everfeeds.Entry
import everfeeds.Filter

/**
 * @author Dmitry Kurinskiy
 * @since 04.05.11 14:25
 */
class FilterHelper {
  static String asJavascript(FilterFace filter) {
    """{
        ${filter instanceof Filter ? 'filter: "'+filter.id+'",' : ""}
        ${filter.access ? 'access:' + filter.access.id + "," : ''}
        wtag: ${filter.withTags*.id.encodeAsJavaScript()},
        wotag: ${filter.withoutTags*.id.encodeAsJavaScript()},
        wcat: ${filter.withCategories*.id.encodeAsJavaScript()},
        wocat: ${filter.withoutCategories*.id.encodeAsJavaScript()},
        wkind: ${filter.withKinds.encodeAsJavaScript()},
        wokind: ${filter.withoutKinds.encodeAsJavaScript()},
        ${filter.getNew ? "newTime:${System.currentTimeMillis()}," : ""}
        listTime: ${filter?.splitDate?.time ?: 0},
    }"""
  }

  static List<Entry> findEntries(FilterFace filter, Map listParams) {
    if (!listParams.containsKey("sort")) listParams["sort"] = "placedDate"
    if (!listParams.containsKey("order")) listParams["order"] = "desc"

    def criteria

    if (filter.access) {
      Entry.findAllFiltered(filter).list(listParams)
    } else {
      Entry.findAllFilteredByAccount(filter).list(listParams)
    }
  }
}
