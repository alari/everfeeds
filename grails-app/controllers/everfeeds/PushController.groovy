package everfeeds

import everfeeds.envelops.EntryEnvelop
import grails.plugins.springsecurity.Secured

class PushController {

  @Secured(['ROLE_ACCOUNT'])
  def access = {
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
  def absolute = {
    render "not implemented yet"
  }
}
