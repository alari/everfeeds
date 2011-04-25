package everfeeds.bootstrap

import everfeeds.Entry

/**
 * @author Dmitry Kurinskiy
 * @since 25.04.11 20:27
 */
class MongoIndicesBoot {
  static run(){
    Entry.collection.ensureIndex([accessId:1, identity: 1, kind: 1], [unique:true, dropDups:true])
  }
}
