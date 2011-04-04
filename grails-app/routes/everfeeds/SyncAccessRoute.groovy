package everfeeds

class SyncAccessRoute {
    def configure = {
        from('activemq:sync.access').to('bean:syncService?method=syncAccess')
    }
}
