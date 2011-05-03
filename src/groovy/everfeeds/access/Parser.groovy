package everfeeds.access

import everfeeds.Access
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 0:09
 */
abstract class Parser {
  public Accessor accessor

  public Access getAccess() {
    accessor.access
  }

  abstract public EntryEnvelop parseEntry(String categoryIdentity, final node)

  public EntryEnvelop parseFromParams(params){
    params.remove("access")
    params.remove("action")
    params.remove("controller")

    EntryEnvelop envelop = new EntryEnvelop(params as Map)
    envelop.accessId = access.id
    envelop
  }
}
