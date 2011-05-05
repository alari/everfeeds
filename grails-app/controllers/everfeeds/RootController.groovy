package everfeeds

import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.FilterEnvelop
import grails.plugins.springsecurity.Secured

class RootController {
  def syncService
  def i18n

  def index = {
    if (loggedIn) {
      List<Access> expiredAccesses = Access.findAllByAccountAndExpired(authenticatedUser, true)

      def tabsAccesses = authenticatedUser.accesses.findAll{it.accessor.isPushable() || it.accessor.isPullable()}

      List<Filter> filters = Filter.findAllByAccountId(authenticatedUser.id)
      render view: "authIndex", model: [account: authenticatedUser, expiredAccesses: expiredAccesses, tabsAccesses:tabsAccesses, filters: filters]
      return
    }
  }

  @Secured(['ROLE_ACCOUNT'])
  def push = {
    Access access = Access.findByIdAndAccount(params.long("access"), authenticatedUser)
    if (!access) {
      response.sendError HttpURLConnection.HTTP_UNAUTHORIZED, "not authorized enough"
      return
    }
    log.debug "processing push"

    try {

      EntryEnvelop envelop = access.accessor.parser.parseFromParams(params)

      if (envelop) {
        envelop = access.accessor.push(envelop)
        Entry entry = envelop.store()
        render template: "/entry/envelop", model: [entry: entry]
        return
      }
    } catch (e) {
      log.error "Push failed", e
      response.sendError(HttpURLConnection.HTTP_BAD_REQUEST, e.message)
    }
  }

  @Secured(['ROLE_ACCOUNT'])
  def entries = {
    // TODO: move it to separate action
    if (params.content) {
      Entry e = Entry.findByIdAndAccountId(params.content, authenticatedUser.id)
      render e?.content ?: "E not found for ${params.content}, ${authenticatedUser}"
      return;
    }

    Access access = Access.findByIdAndAccount(params.long("access"), authenticatedUser)
    List<Entry> entries

    FilterEnvelop filter = new FilterEnvelop()
    filter.account = authenticatedUser
    filter.buildFromParams(params, access)

    int max = 10
    int page = params.page ? params.int("page") : 0

    entries = filter.findEntries(max: max, offset: page * max)

    if (!page) {
      if (!params?.getNew && access?.accessor?.isPushable()) {
        render template: "/push/${access.type}", model: [access: access]
      }
      render template: "checkNew"
    }

    render template: "entries", model: [entries: entries]

    if (access) {
      render template: "filterAside", model: [filter: filter]
    }

    render template: "tabJsCache", model: [filter: filter]

    if (entries.size()) {
      render template: "loadMore", model: [page: page]
    }
  }
}
