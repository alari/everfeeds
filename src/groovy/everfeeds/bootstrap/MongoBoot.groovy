package everfeeds.bootstrap

import everfeeds.AccountRole
import everfeeds.Access

/**
 * Created by alari @ 14.04.11 18:15
 */
class MongoBoot {
    static run(){
        Access.collection.drop()
        //to ensure complex indexes
        AccountRole.collection.ensureIndex([account:1, role:1], [unique:true, dropDups:true])
    }
}
