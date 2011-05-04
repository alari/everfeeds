package everfeeds.access.gmail.kind

import everfeeds.access.Kind

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 20:08
 */
class GmailEmail extends Kind {
  String getIdentity() {
    original.id
  }

  String getContent() {
    original.summary.text()
  }

  String getSourceUrl() {
    original.link.@href
  }

  String getAuthor() {
    original.author.name.text()
  }

  String getAuthorIdentity(){
    original.author.email.text()
  }

  boolean getIsPublic() {
    true
  }

  String getTitle() {
    original.title.text()
  }

  Date getPlacedDate() {
    // TODO: is it really do not give placed date?
    new Date()
  }
}
