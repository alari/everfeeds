package everfeeds.access.evernote

import everfeeds.access.Parser
import everfeeds.access.evernote.kind.EvernoteNote
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:08
 */
class EvernoteParser extends Parser {
  EntryEnvelop parseEntry(node) {
    new EvernoteNote().initEntryEnvelop(node, accessor).buildEnvelop().entryEnvelop
  }

  EntryEnvelop parseEntry(String categoryIdentity, node) {
    parseEntry(node)
  }
}
