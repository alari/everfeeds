package everfeeds.access.greader.kind

import everfeeds.access.Kind

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 19:54
 */
class GreaderAtom extends Kind {

  String getIdentity() {
    original.id
  }

  String getAuthor() {
    original.author
  }

  boolean getIsPublic() {
    true
  }

  String getSourceUrl() {
    original.alternate.find {it.type == "text/html"}?.href
  }

  String getTitle() {
    original.title
  }

  String getContent() {
    original.content?.content ?: original.summary?.content?.replace("\n", "<br/>")
  }

  String getKind() {
    "atom"
  }

  Date getPlacedDate() {
    new Date(((long) original.updated) * 1000)
  }

  List<String> getTagIdentities() {
    original.categories.collect {it.toString()}
  }
}
