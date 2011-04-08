package everfeeds.access.envelops

import everfeeds.access.IFilter
import everfeeds.*

/**
 * Created by alari @ 08.04.11 13:14
 */
class FilterEnvelop implements IFilter{
    Access access
    Tag[] withTags = []
    Tag[] withoutTags = []
    Category[] withCategories = []
    Category[] withoutCategories = []
    Account account

    List<Entry> findEntries(Map listParams = [:]) {
        if(!listParams.containsKey("sort")) listParams["sort"] = "placedDate"
        if(!listParams.containsKey("order")) listParams["order"] = "desc"
        if(access){
            Entry.findAllFiltered(this).list(listParams)
        } else {
            Entry.findAllByAccount(account, listParams ?: [sort: "placedDate", order: "desc"])
        }
    }
}
