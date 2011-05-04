package everfeeds.access.facebook.kind

import org.springframework.stereotype.Component

/**
 * @author Boris G. Tsirkin
 * @since 04.05.2011
 */
@Component
class FacebookNews extends FacebookStatus {
  String getTitle() {
    original?.caption
  }
}
