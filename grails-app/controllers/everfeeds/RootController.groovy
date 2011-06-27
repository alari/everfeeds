package everfeeds

import grails.plugins.springsecurity.Secured
import everfeeds.thrift.domain.Access

class RootController {
  def syncService
  def thriftApiService
  def i18n

  def index = {
    if (loggedIn) {
      List<Access> accesses = thriftApiService.api.getAccesses(authenticatedUser.token)
      List<Access> expiredAccesses = accesses.findAll{it.expired}

      render view: "authIndex", model: [
          account: authenticatedUser,
          expiredAccesses: expiredAccesses,
          tabsAccesses:accesses]
      return
    }
    log.debug "we are not logged in"
  }

  @Secured(['ROLE_ACCOUNT'])
  def entries = {/*
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
    }   */
  }
}
