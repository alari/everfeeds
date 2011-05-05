package everfeeds.access.facebook.kind

import org.springframework.stereotype.Component

/**
 * @author Boris G. Tsirkin<mail@dotbg.name>
 * @since 4.5.2011
 */
@Component
class FacebookPhoto extends FacebookStatus {

  String getTitle() {
    "Photo by ${getAuthor()}"?:''
  }

  String getContent() {
    original?.message
  }
}
