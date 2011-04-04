package everfeeds

class SyncPullAccessRoute {
    def configure = {
        from('activemq:sync.pull.access').to('bean:syncService?method=pullAccess')
    }
}
