package everfeeds

import everfeeds.access.envelops.EntryEnvelop

class SyncService {

    static transactional = true

    void addToQueue(Access access, boolean pull = false) {
        log.debug "Adding access ${access} to queue, pull=${pull}"
        sendMessage("seda:sync.access", [id: access.id, pull: pull])
    }

    void addToQueue(Account account, boolean pull = false) {
        log.debug "Adding account ${account} to queue, pull=${pull}"
        account.accesses.each {
            addToQueue it, pull
        }
    }

    void addToQueue(EntryEnvelop entry) {
        log.debug "Adding entry ${entry.identity} to queue"
        sendMessage("seda:sync.entry", entry)
    }

    boolean syncAccess(Map params) {
        def id = params.id
        def pull = params.pull
        log.debug "Processing syncAccess(${id})"
        Access access = Access.get(id)
        access.manager.sync()
        access.lastSync = new Date()
        access.save()
        log.debug "Sync complete"
        if (pull) {
            sendMessage("seda:sync.pull.access", id)
        }
        true
    }

    boolean pullAccess(id) {
        log.debug "Processing pullAccess(${id})"
        Access access = Access.get(id)
        log.debug "Access type: ${access.type}, access class: ${access.manager.class.canonicalName}"
        access.manager.pull([store:true])
        log.debug "pullAccess(${id}) finished"
        true
    }
}
