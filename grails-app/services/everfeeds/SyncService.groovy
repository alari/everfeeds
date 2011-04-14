package everfeeds

class SyncService {
    static transactional = "mongo"

    void addToQueue(Access access, Map params = [:]) {
        log.debug "Adding access ${access} to queue, pull=${params.pull}"
        params.id = access.id.toString()
        sendMessage("activemq:sync.access", params)
    }

    void addToQueue(Account account, Map params = [:]) {
        log.debug "Adding account ${account} to queue, pull=${params.pull}"
        account.accesses.each {
            addToQueue it, params
        }
    }

    boolean syncAccess(Map params) {
        def id = params.id
        def pull = params.pull
        def num = params.num
        log.debug "Processing syncAccess(${id})"
//        log.debug Access.list()*.authenticity.join(" ")

        System.err << Access.list()
        Access access = Access.get(id)
        log.debug access
        access.accessor.sync()
        access.lastSync = new Date()
        access.save()
        log.debug "Sync complete"
        if (pull) {
            sendMessage("activemq:sync.pull.access", [id:id, num:num])
        }
        true
    }

    boolean pullAccess(Map params) {
        log.debug "Processing pullAccess(${params.id})"
        Access access = Access.get(params.id)
        log.debug "Access type: ${access.type}, access class: ${access.accessor.class.canonicalName}"
        access.accessor.pull([store:true, num:params?.num])
        log.debug "pullAccess(${params.id}) finished"
        true
    }
}
