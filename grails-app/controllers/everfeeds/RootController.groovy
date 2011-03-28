package everfeeds

import grails.plugins.springsecurity.Secured
import grails.converters.JSON

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
            render "<script>setAccess('${access.id}');</script>"
        }
    }

    @Secured(['ROLE_ACCOUNT'])
    def loadAccess = {
        Access access = Access.findByIdAndAccount(params.id, authenticatedUser)
        log.debug "Hey ${params.id} / ${access}"
        render([categories: access.categories.collect{[it.id, it.title]}, tags: access.tags.collect{[it.id, it.title]}] as JSON)
    }
}
