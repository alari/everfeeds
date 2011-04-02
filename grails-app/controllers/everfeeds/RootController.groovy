package everfeeds

import grails.plugins.springsecurity.Secured

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
        def entries
        Map filterParams = [
                access: access,
                withTags: [],
                withoutTags: [],
                withCategories: [],
                withoutCategories: [],
        ]
        if (access) {
            Closure filterParam = {what, param ->
                params.list(param + "[]").collect {p -> access."${what}".find {i -> i.id == Long.parseLong(p.toString())}}
            }
            filterParams.withTags = filterParam("tags", "wtag")
            filterParams.withoutTags = filterParam("tags", "wotag")

            filterParams.withCategories = filterParam("categories", "wcat")
            filterParams.withoutCategories = filterParam("categories", "wocat")

            entries = Entry.findAllFiltered(filterParams).list()
        } else {
            entries = Entry.findAllByAccount(authenticatedUser, [sort: "placedDate", order: "desc"])
        }
        render template: "entries", model: [entries: entries]
        if (access) {
            filterParams.random = new Random().nextInt().toString()
            filterParams.testClass = {obj, with, without -> with.contains(obj) ? "with" : (without.contains(obj) ? "without" : "")}
            render template: "filterAside", model: filterParams
        }
    }
}
