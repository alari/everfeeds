package everfeeds

import everfeeds.envelops.FilterEnvelop
import grails.converters.JSON
import grails.plugins.springsecurity.Secured

class FilterController {
  @Secured(['ROLE_ACCOUNT'])
  def save = {
    Access access = Access.findByIdAndAccount(params.long("access"), authenticatedUser)

    FilterEnvelop filter = new FilterEnvelop()
    filter.account = authenticatedUser
    filter.buildFromParams(params, access)
    filter.title = params.title ?: "New Filter"

    Filter newFilter = filter.store()

    render([url: createLink(controller: "filter", action: "entries", id: newFilter.id), title: newFilter.title] as JSON)
  }

  @Secured(['ROLE_ACCOUNT'])
  def entries = {

    Filter filter = Filter.findByAccountIdAndId(authenticatedUser.id, params.id)

    if (!filter?.id) {
      response.sendError HttpURLConnection.HTTP_NOT_FOUND, "not found"
      return
    }

    filter.splitDate = new Date()

    int max = 10
    int page = params.page ? params.int("page") : 0

    def entries = filter.findEntries(max: max, offset: page * max)

    if(!page) {
      render template: "/root/checkNew"
    }

    render template: "/root/entries", model: [entries: entries]

    render template: "/root/filterAside", model: [filter: filter]

    render template: "/root/tabJsCache", model: [filter: filter]

    if (entries.size()) {
      render template: "/root/loadMore", model: [page: page]
    }
  }
}
