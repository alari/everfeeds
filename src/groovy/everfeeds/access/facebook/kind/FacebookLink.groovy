package everfeeds.access.facebook.kind

import org.springframework.stereotype.Component

/**
 * @author Boris G. Tsirkin<mail@dotbg.name>
 * @since 4.5.2011
 */
@Component
class FacebookLink extends FacebookStatus {
  String getTitle() {
    "Link ${getAuthor()} ${original?.message ?: ''}"
  }

}
