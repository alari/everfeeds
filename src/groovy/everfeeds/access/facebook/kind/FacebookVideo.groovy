package everfeeds.access.facebook.kind

import org.springframework.stereotype.Component

/**
 * @author Boris G. Tsirkin <mail@dotbg.name>
 * @since 04.05.2011
 */
@Component
class FacebookVideo extends FacebookStatus {
  String getTitle() {
    "Video ${getAuthor()}"
  }
}
