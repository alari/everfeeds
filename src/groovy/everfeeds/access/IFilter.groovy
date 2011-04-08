package everfeeds.access

import everfeeds.*
import org.codehaus.groovy.grails.orm.hibernate.cfg.NamedCriteriaProxy

/**
 * Created by alari @ 08.04.11 13:07
 */
public interface IFilter {
    public Access getAccess()

    //public NamedCriteriaProxy findEntries()

    public Tag[] getWithTags()
    public Tag[] getWithoutTags()

    public Category[] getWithCategories()
    public Category[] getWithoutCategories()
}