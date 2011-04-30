/**
 * Directory configuration.
 * Pickup the Tomcat/Catalina directory else use the target or current dir.
 */
def fs = File.separator // Local variable.
def globalDirs = [:]
globalDirs.targetDir = new File("target${fs}").isDirectory() ? "target${fs}" : ''
globalDirs.catalinaBase = System.properties.getProperty('catalina.base')
globalDirs.logDirectory = globalDirs.catalinaBase ? "${globalDirs.catalinaBase}${fs}logs${fs}" : globalDirs.targetDir
def appName = "everfeeds"
/**
 * Log4j configuration.
 * Causing this file to reload (e.g. edit+save) may break the appLog destination
 * and further logs will be written to files or directories like "[:]".
 * For more info see http://logging.apache.org/log4j/1.2/manual.html
 * For log levels see http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Level.html
 * Basic log levels are ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
 */
log4j = {
  appenders {
    // Use if we want to prevent creation of a stacktrace.log file.
    'null' name: 'stacktrace'

    // Use this if we want to modify the default appender called 'stdout'.
    console name: 'stdout', layout: pattern(conversionPattern: '[%t] %-5p %c{2} %x - %m%n')

    // Custom log file.
    rollingFile name: "appLog",
        file: "${globalDirs.logDirectory}${appName}.log".toString(),
        maxFileSize: '300kB',
        maxBackupIndex: 1,
        layout: pattern(conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n')
    System.out.println("Logging to ${globalDirs.logDirectory}${appName}.log".toString())
  }

  // This is for the built-in stuff and from the default Grails-1.2.1 config.
  error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
      'org.codehaus.groovy.grails.web.pages', //  GSP
      'org.codehaus.groovy.grails.web.sitemesh', //  layouts
      'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
      'org.codehaus.groovy.grails.web.mapping', // URL mapping
      'org.codehaus.groovy.grails.commons', // core / classloading
      'org.codehaus.groovy.grails.plugins', // plugins
      'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
      'org.springframework',
      'org.hibernate',
      'net.sf.ehcache.hibernate'

  warn 'org.mortbay.log' // Jetty

  all 'everfeeds'
  warn 'net.bull.javamelody'

  all 'grails.app' // Set the default log level for our app code.
  //error 'grails.app.bootstrap' // Set the log level per type and per type.class
  //error 'grails.app.service.AuthService'
  //error 'grails.app.service.NavigationService'
  //error 'grails.app.service.com.zeddware.grails.plugins.filterpane.FilterService'
  //info 'org.codehaus.groovy.grails.plugins.searchable'
  //info 'org.compass'
  //error 'grails.app.task' // Quartz jobs.
  //info 'grails.app.task.InventoryIndexJob'

  // Move anything that should behave differently into this section.
  switch (application.config.environment) {
    case 'development':
      // Configure the root logger to output to stdout and appLog appenders.
      root {
        error 'stdout', 'appLog'
        additivity = true
      }
      //debug "org.hibernate.SQL"
      //debug 'grails.app.service'
      //debug 'grails.app.controller'
      break
    case 'test':
      // Configure the root logger to only output to appLog appender.
      root {
        error 'stdout', 'appLog'
        additivity = true
      }
      //debug 'grails.app.service'
      //debug 'grails.app.controller'
      break
    case 'production':
      // Configure the root logger to only output to appLog appender.
      root {
        error 'stdout', 'appLog'
        additivity = true
      }
      //warn 'grails.app.service'
      //warn 'grails.app.controller'
      break
  }
}
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = false