package everfeeds.access.facebook.kind

import everfeeds.access.Kind
import java.text.SimpleDateFormat
import org.springframework.stereotype.Component
import org.apache.commons.lang.StringUtils

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 19:21
 */
@Component
class FacebookStatus extends Kind {
  private static DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  @Override
  String getIdentity() {
    original.id
  }

  String getSourceUrl() {
    original.source ?: (original.link ?: "http://facebook.com/${original.id}")
  }

  String getContent() {
    (original?.message ?: "") + " " + (original?.description ?: "")
  }

  String getAuthor() {
    original.from?.name ?: "Unknown"
  }

  String getAuthorIdentity(){
    original.from?.id ?: ""
  }

  boolean getAccessIsAuthor(){
    original.from?.id?.toString() == accessor.access.identity
  }

  boolean getIsPublic(){
    true
  }

  String getImageUrl() {
    original?.picture ?: original?.icon ?: "http://facebook.com/${original.id}/picture"
  }

  String getTitle() {
    "${StringUtils.capitalise(getKind())} ${getAuthor()}"//TODO: I18N
  }

  Date getPlacedDate() {
    (original?.created_time) ? DATE_FORMAT.parse(original?.created_time) : new Date()
  }
}
