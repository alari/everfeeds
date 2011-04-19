package everfeeds.access.evernote

import everfeeds.access.Parser
import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 1:08
 */
class EvernoteParser extends Parser {
  EntryEnvelop parseEntry(node) {
    parseEntry node.notebookGuid, node
  }

  EntryEnvelop parseEntry(String categoryIdentity, Object node) {
    node = (com.evernote.edam.type.Note)node
    new EntryEnvelop(
        title: node.title,
        content: accessor.getNoteContent(node.guid),
        identity: node.guid,
        author: node.attributes.author,
        tagIdentities: node.tagGuids,
        categoryIdentity: node.notebookGuid,
        sourceUrl: node.attributes.sourceURL,
        placedDate: new Date(node.created),
        accessId: access.id
    )
  }
}
