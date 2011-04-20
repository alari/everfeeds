package everfeeds.access.greader

import everfeeds.access.Parser
import everfeeds.access.greader.kind.GreaderAtom
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 0:17
 */
class GreaderParser extends Parser {

  EntryEnvelop parseEntry(node) {
    parseEntry node.origin.streamId, node
  }

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    new GreaderAtom().newEntryEnvelop(node, accessor, categoryIdentity).buildEnvelop().entryEnvelop
  }

}
