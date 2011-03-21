package everfeeds

class SyncAccessRoute {
    def configure = {
        from('seda:sync.access').to('bean:syncService?method=syncAccess')
    }
}
