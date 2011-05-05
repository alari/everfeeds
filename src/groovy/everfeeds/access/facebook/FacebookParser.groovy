package everfeeds.access.facebook

import everfeeds.access.Parser

import everfeeds.envelops.EntryEnvelop
import everfeeds.access.Manager

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:04
 */
class FacebookParser extends Parser {

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    // here we should dispatch all kinds of facebook entries

    Class kindClass = Manager.classForKind("facebook", node['type']);
    if (kindClass == null) {
      kindClass = Manager.classForKind("facebook", "status");
    }
    return kindClass.newInstance().initEntryEnvelop(node, accessor, categoryIdentity).buildEnvelop().entryEnvelop;
  }
}
