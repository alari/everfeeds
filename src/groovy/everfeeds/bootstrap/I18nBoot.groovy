package everfeeds.bootstrap

import everfeeds.I18n

/**
 * Created by alari @ 06.04.11 15:27
 */
class I18nBoot {
  static run() {
    I18n.metaClass.'static'.invokeMethod = {String code, args ->
        System.err << "Deprecated: I18n.\"${code}\"\n"
        I18n._.methodMissing(code, args)
    }
  }
}
