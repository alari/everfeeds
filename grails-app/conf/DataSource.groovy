// environment specific settings
environments {
    development {

    }
    test {
        grails {
                mongo {
                    databaseName = "testEverfeeds"
                }
            }
    }
  production {

  }
}