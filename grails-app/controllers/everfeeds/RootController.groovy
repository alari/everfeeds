package everfeeds

import grails.plugins.springsecurity.Secured

class RootController {
    def syncService

    def index = {
        if(loggedIn) {
            render view: "authIndex", model: [account: authenticatedUser]
            return
        }
    }

    @Secured(['ROLE_ACCOUNT'])
    def entries = {
        Access access = Access.findByIdAndAccount(params.access, authenticatedUser)
        def entries
        if(access) {
            List<Tag> taglist = []
            params.list("tag").each { t->
                    taglist.add access.tags.find{it.id == Long.parseLong(t.toString())}
            }
            Category category = params.category ? access.categories.find{it.id == params.long("category")} : null
            entries = Entry.findAllFiltered(access:access, withTags:taglist, withCategories: category?[category]:[]).list()
        } else {
            entries = Entry.findAllByAccount(authenticatedUser, [sort:"placedDate", order: "desc"])
        }
        render template: "entries", model: [entries: entries]
        if(access) {
            render template: "sideRightCategs", model: [categories:access.categories,tags:access.tags]
        }
    }
}
