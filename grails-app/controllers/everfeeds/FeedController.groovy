package everfeeds

import grails.plugins.springsecurity.Secured

@Secured('IS_AUTHENTICATED_REMEMBERED')
class FeedController {

    def index = {
        [accesses: principal.accesses, feeds: principal.accesses*.feeds]
    }

    def create = {
        [access: principal.accesses.find{it.id=params.id}]
    }
}
