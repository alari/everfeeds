grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"

      mavenRepo "http://maven.everfeeds.com/libs-snapshot"
      mavenRepo "http://maven.everfeeds.com/libs-release-local"
      mavenRepo "http://maven.twttr.com/"
    }
    dependencies {
      build "com.everfeeds:thrift-internal:1.0-SNAPSHOT"
      build "org.scribe:scribe:1.2.1"
      build "com.evernote:en-thrift:1.19.0.3"
      build "org.jsoup:jsoup:1.6.0"
      build "net.sourceforge.htmlcleaner:htmlcleaner:2.2"
      build "commons-codec:commons-codec:1.5"
      build "com.twitter:twitter-text:1.3.1"
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }
}
