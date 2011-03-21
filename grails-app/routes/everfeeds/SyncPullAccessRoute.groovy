package everfeeds

class SyncPullAccessRoute {
    def configure = {
        from('seda:sync.pull.access').to('bean:syncService?method=pullAccess')
    }
}
