package everfeeds.access.twitter.kind

import com.twitter.Autolink
import everfeeds.access.Kind
import java.text.SimpleDateFormat

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 19:32
 */
class TwitterStatus extends Kind {

  static protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
  static protected Autolink autolink = new Autolink()

  static public String getTemplate() {
    "/entry/twitter"
  }

  String getIdentity() {
    original.id
  }

  String getAuthor() {
    original.user.screen_name
  }

  String getAuthorIdentity(){
    original.user.id
  }

  boolean getAccessIsAuthor(){
    original.user.id.toString() == accessor.access.identity
  }

  boolean getIsPublic(){
    ! original.user?.protected
  }

  String getSourceUrl() {
    "http://twitter.com/${author}/status/${identity}"
  }

  String getImageUrl() {
    original.user.profile_image_url
  }

  String getTitle() {
    original.text
  }

  String getContent() {
    autolink.autoLink(title)
  }

  String getKind() {
    "status"
  }

  Date getPlacedDate() {
    simpleDateFormat.parse(original.created_at)
  }

  List<String> getTagIdentities() {
    List<String> tags = []
    accessor.TAGS.each {tagId, tagData ->
      if (tagData.check(original)) tags.add tagId
    }
    tags
  }
}
