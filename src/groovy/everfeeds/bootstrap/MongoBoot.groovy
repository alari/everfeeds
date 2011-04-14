package everfeeds.bootstrap

import everfeeds.AccountRole

/**
 * Created by alari @ 14.04.11 18:15
 */
class MongoBoot {
    static run(){
        //to ensure complex indexes
        AccountRole.collection.ensureIndex([account:1, role:1], [unique:true, dropDups:true])
    }
}
