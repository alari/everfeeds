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
      def pushAccesses = authenticatedUser.accesses.findAll{it.accessor.isPushable() && it.accessor.kinds.contains("status")}

      List<Filter> filters = Filter.findAllByAccountId(authenticatedUser.id)
      render view: "authIndex", model: [
          account: authenticatedUser,
          expiredAccesses: expiredAccesses,
          pushAccesses: pushAccesses,
          tabsAccesses:tabsAccesses,
          filters: filters]
      return
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
