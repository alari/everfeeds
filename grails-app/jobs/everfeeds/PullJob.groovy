package everfeeds



class PullJob {
    def syncService

    static triggers = {
      simple repeatInterval: 500000l // execute job once in 5 seconds
    }

    def execute() {
        Access.findAllByExpiredAndLastSyncLessThan(false, new Date(System.currentTimeMillis()-600000l))?.each {
            syncService.addToQueue it, [pull: true]
        }
    }
}
