package everfeeds.access.evernote.kind

import com.evernote.edam.type.Note
import everfeeds.access.Kind
import everfeeds.access.evernote.EvernoteAccessor

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 18:53
 */
class EvernoteNote extends Kind {
  protected Note getOriginal() {
    super.original
  }

  String getIdentity() {
    original.guid
  }

  String getTitle() {
    original.title
  }

  String getContent() {
    ((EvernoteAccessor) accessor).getNoteContent(original.guid)
  }

  String getAuthor() {
    original.attributes.author
  }

  String getKind() {
    "note"
  }

  String getSourceUrl() {
    original.attributes.sourceURL
  }

  Date getPlacedDate() {
    new Date(original.created)
  }

  String getCategoryIdentity() {
    original.notebookGuid
  }

  List<String> getTagIdentities() {
    original.tagGuids
  }
}
