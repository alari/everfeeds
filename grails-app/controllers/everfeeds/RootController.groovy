package everfeeds

import grails.plugins.springsecurity.Secured
import everfeeds.envelops.FilterEnvelop

class RootController {
    def syncService

    def index = {
        if (loggedIn) {
            render view: "authIndex", model: [account: authenticatedUser]
            return
        }
    }

    @Secured(['ROLE_ACCOUNT'])
    def entries = {
        Access access = Access.findByIdAndAccount(params.access, authenticatedUser)
        List<Entry> entries
        FilterEnvelop filter = new FilterEnvelop()
        filter.account = authenticatedUser
        def max = 10
        def page = params.page ? params.int("page") : 0

        if (access) {
            setFilterParams filter, access
        }
        setFilterSplitDate filter

        entries = filter.findEntries(max: max, offset: page*max)

        if(!page) {
            render template: "checkNew"
        }

        render template: "entries", model: [entries: entries]

        if (access) {
            render template: "filterAside", model: [filter:filter]
        }

        if(entries.size() >= max-1) {
            render template: "loadMore", model: [page: page]
        }
    }

    private setFilterSplitDate(FilterEnvelop filter){
        if(params?.getNew) {
            filter.getNew = true
            filter.splitDate = new Date(params.long("newTime") ?: (params.long("listTime") ?: System.currentTimeMillis()))
        } else {
            filter.splitDate = new Date(params.listTime ? params.long("listTime") : System.currentTimeMillis())
        }
    }

    private setFilterParams(FilterEnvelop filter, Access access) {
            filter.access = access
            filter.withTags = filterParam("tags", "wtag", access)
            filter.withoutTags = filterParam("tags", "wotag", access)

            filter.withCategories = filterParam("categories", "wcat", access)
            filter.withoutCategories = filterParam("categories", "wocat", access)
    }

    private List filterParam(what, param, access){
        params.list(param + "[]").collect {p -> access."${what}".find {i -> i.id == Long.parseLong(p.toString())}}
    }
}
