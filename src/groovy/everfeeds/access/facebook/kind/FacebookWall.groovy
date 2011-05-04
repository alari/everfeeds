package everfeeds.access.facebook.kind

/**
 * @author Boris G. Tsirkin<mail@dotbg.name>
 * @since 4.5.2011
 */
class FacebookWall extends FacebookStatus {
  @Override
  String getTitle() {
    original?.name?:original?.caption
  }

}
