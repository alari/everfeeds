package everfeeds

import grails.plugins.springsecurity.Secured
import everfeeds.envelops.FilterEnvelop

class RootController {
    def syncService
    def i18n

    def index = {
        if (loggedIn) {
            render view: "authIndex", model: [account: authenticatedUser]
            return
        }
    }

    @Secured(['ROLE_ACCOUNT'])
    def entries = {
        // TODO: move it to separate action
        if(params.content) {
            System.err << "Requesting full entry content...\n"
            Entry e = Entry.findByIdAndAccountId(params.content, authenticatedUser.id)
            render e?.content ?: "E not found for ${params.content}, ${authenticatedUser}"
            return;
        }

        Access access = Access.findByIdAndAccount(params.long("access"), authenticatedUser)
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

      render template: "tabJsCache", model: [filter:filter]

        if(entries.size()) {
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
