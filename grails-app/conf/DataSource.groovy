import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration

dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            configClass = GrailsAnnotationConfiguration.class
            dbCreate = "create-drop"
            username = "postgres"
            password = "12345"
            url = "jdbc:postgresql://localhost/everfeeds" //:file:prodDb;shutdown=true"
            driverClassName = "org.postgresql.Driver"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
  production {
    dataSource {
      configClass = GrailsAnnotationConfiguration.class
      dbCreate = "update"
      username = "postgres"
      url = "jdbc:postgresql://localhost/everfeeds" //:file:prodDb;shutdown=true"
      driverClassName = "org.postgresql.Driver"
    }
  }
}
