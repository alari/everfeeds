package everfeeds.access

import everfeeds.envelops.EntryEnvelop

/**
 * @author Dmitry Kurinskiy
 * @since 20.04.11 18:35
 */
abstract class Kind {
  protected original
  protected EntryEnvelop entryEnvelop
  protected Accessor accessor
  private categoryIdentityPreset

  static public String getTemplate() {
    "/entry/default"
  }

  public newEntryEnvelop(original, Accessor accessor, String categoryIdentity = null) {
    this.original = original
    this.accessor = accessor
    entryEnvelop = new EntryEnvelop()
    categoryIdentityPreset = categoryIdentity
    this
  }

  public buildEnvelop() {
    beforeBuildEnvelop()

    ['identity',
        'title',
        'kind',
        'imageUrl',
        'description',
        'content',
        'author',
        'authorIdentity',
        'accessIsAuthor',
        'isPublic',
        'sourceUrl',
        'placedDate',
        'accessId',
        'categoryIdentity',
        'tagIdentities'].each {
      entryEnvelop."${it}" = this."${it}"
    }

    afterBuildEnvelop()
    this
  }

  public store() {
    entryEnvelop.store()
  }

  public EntryEnvelop getEntryEnvelop() {
    entryEnvelop
  }

  protected afterBuildEnvelop() {}

  protected beforeBuildEnvelop() {}


  abstract String getIdentity()

  abstract String getTitle()

  abstract String getKind()

  String getSourceUrl() {""}

  abstract Date getPlacedDate()

  abstract boolean getIsPublic()

  String getCategoryIdentity() {categoryIdentityPreset}

  String getImageUrl() {""}

  String getDescription() {""}

  String getContent() {""}

  String getAuthor() {""}

  String getAuthorIdentity() {""}

  boolean getAccessIsAuthor() {null}

  int getAccessId() {accessor.access.id}

  List<String> getTagIdentities() {[]}
}
