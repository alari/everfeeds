package everfeeds.access.twitter.kind

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 19:44
 */
class TwitterDm extends TwitterStatus {
  static public String getTemplate() {
    "/entry/twitterDM"
  }

  String getKind() {
    "dm"
  }

  String getAuthor() {
    original.sender.screen_name
  }

  String getAuthorIdentity(){
    original.sender.id
  }

  boolean getAccessIsAuthor(){
    original.sender.id.toString() == accessor.access.identity
  }

  boolean getIsPublic(){
    true
  }

  String getSourceUrl() {
    ""
  }

  String getImageUrl() {
    original.sender.profile_image_url
  }

  Date getPlacedDate() {
    simpleDateFormat.parse(original.sender.created_at)
  }
}
