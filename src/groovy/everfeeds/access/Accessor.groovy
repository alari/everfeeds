package everfeeds.access

import everfeeds.envelops.CategoryEnvelop
import everfeeds.envelops.EntryEnvelop
import everfeeds.envelops.EntryFace
import everfeeds.envelops.TagEnvelop
import grails.converters.deep.JSON
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.json.JSONElement
import org.scribe.model.Verb
import everfeeds.*

/**
 * @author Dmitry Kurinskiy
 * @since 14.03.11 15:00
 */
abstract class Accessor {
  protected Access access

  private String typeCache

  private Parser parserCache

  protected log = Logger.getLogger(this.class)

  static protected Map<String, List<String>> kindsCache = [:]

  public Access getAccess() {
    access
  }

  public Map getConfig() {
    Manager.getConfig(type)
  }

  public Parser getParser() {
    if (!parserCache) {
      parserCache = Manager.classForSuffix(type, "Parser").newInstance()
      parserCache.accessor = this
    }
    parserCache
  }

  public List<String> getKinds() {
    if (!kindsCache[type]) {
      kindsCache[type] = []
      Package.getClasses(this.class.package.name + ".kind")?.each {
        kindsCache[type] << it.simpleName.substring(type.size()).toLowerCase()
      }
    }
    kindsCache[type]
  }

  public void sync() {
    List<TagEnvelop> actualTags = tags
    List<CategoryEnvelop> actualCategories = categories

    Set<Tag> currentTags = access.tags
    Set<Category> currentCategories = access.categories

    if (access.expired) return;

    TagEnvelop tag
    CategoryEnvelop category

    currentTags.each { t ->
      tag = actualTags.find {it.identity == t.identity}
      if (!tag) {
        access.removeFromTags t
        t.delete()
      } else {
        if (t.titleIsCode) return;
        t.title = tag.title
        t.save()
        actualTags.remove(tag)
      }
    }
    actualTags.each { t ->
      access.addToTags new Tag(identity: t.identity, title: t.title, access: access, titleIsCode: t.titleIsCode).save()
    }

    currentCategories.each { c ->
      category = actualCategories.find {it.identity == c.identity}
      if (!category) {
        access.removeFromCategories c
        c.delete()
      } else {
        if (c.titleIsCode) return;
        c.title = category.title
        c.save()
        actualCategories.remove(category)
      }
    }
    actualCategories.each { c ->
      access.addToCategories new Category(identity: c.identity, title: c.title, access: access, titleIsCode: c.titleIsCode).save()
    }

    access.lastSync = new Date()
    access.save()
  }

  public String getType() {
    if (!typeCache) {
      typeCache = this.class.package.name.tokenize(".").last()
    }
    typeCache
  }

  final public String callOAuthApi(String url, Verb verb) {
    callOAuthApi(url, [:], verb)
  }

  final public String callOAuthApi(String url) {
    callOAuthApi(url, [:], Verb.GET)

  }

  final public String callOAuthApi(String url, Map<String, String> params) {
    callOAuthApi(url, params, Verb.POST)
  }

  final public String callOAuthApi(String url, Map<String, String> params, Verb verb) {
    try {
      return OAuthHelper.callApi(config.oauth, url, access.token, access.secret, verb, params)
    } catch (e) {
      log.error "OAuth Api call failed", e
    }
    ""
  }

  final public JSONElement callOAuthApiJSON(String url, Verb verb) {
    JSON.parse callOAuthApi(url, verb)
  }

  final public JSONElement callOAuthApiJSON(String url) {
    JSON.parse callOAuthApi(url)
  }

  final public JSONElement callOAuthApiJSON(String url, Map<String, String> params) {
    JSON.parse callOAuthApi(url, params)
  }

  final public JSONElement callOAuthApiJSON(String url, Map<String, String> params, Verb verb) {
    JSON.parse callOAuthApi(url, params, verb)
  }

  abstract public boolean isPullable()

  abstract public boolean isPushable()

  abstract public List<TagEnvelop> getTags()

  abstract public List<CategoryEnvelop> getCategories()

  abstract public List<EntryEnvelop> pull(Map params = [:])

  public EntryEnvelop push(EntryFace entry) {null}
}
