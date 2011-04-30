grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
    xml: ['text/xml', 'application/xml'],
    text: 'text/plain',
    js: 'text/javascript',
    rss: 'application/rss+xml',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    all: '*/*',
    json: ['application/json', 'text/json'],
    form: 'application/x-www-form-urlencoded',
    multipartForm: 'multipart/form-data'
]
grails.tomcat.jvmArgs = ["-Xmx1024m", "-XX:MaxPermSize=256m"]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
grails.views.javascript.library = "jquery"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// set per-environment serverURL stem for creating absolute links
environments {
  production {
    grails.serverURL = "http://everfeeds.com"
  }
  development {
    grails.serverURL = "http://loc.everfeeds.com/${appName}"
    //grails.serverURL = "http://localhost:8080/${appName}"
  }
  test {
    grails.serverURL = "http://localhost:8080/${appName}"
  }
}

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts
grails.config.locations = [ // for local settings
  "file:${userHome}/.${appName}/override-config.groovy"
]

new File('grails-app/conf').eachFileMatch(~/^[A-Z][a-zA-Z]*?Config\.groovy/) { f ->
  grails.config.locations << "classpath:${f.name}"
  System.out.println("Adding to config locations: "+f.name);
}
if(System.properties["${appName}.config.location"]) {
  grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'everfeeds.Account'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'everfeeds.AccountRole'
grails.plugins.springsecurity.authority.className = 'everfeeds.Role'
grails.plugins.springsecurity.requestMap.className = 'everfeeds.AccountRole'
//grails.plugins.springsecurity.securityConfigType = 'Requestmap'
grails.plugins.springsecurity.failureHandler.defaultFailureUrl = '/'
grails.plugins.springsecurity.auth.loginFormUrl = '/'

grails.camel.camelContextId = appName

if (environment == "production") {
  System.setProperty("org.apache.activemq.default.directory.prefix", '/home/tomcat/')
}
