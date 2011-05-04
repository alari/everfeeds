package everfeeds.access.facebook.kind

import org.springframework.stereotype.Component

/**
 * @author Boris G. Tsirkin<mail@dotbg.name>
 * @since 4.5.2011
 */
@Component
class FacebookMusic extends FacebookStatus {
  String getTitle() {
    original?.name
  }

}
