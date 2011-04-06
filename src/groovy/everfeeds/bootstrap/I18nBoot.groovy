package everfeeds.bootstrap

import org.springframework.context.i18n.LocaleContextHolder as LCH

import everfeeds.I18n

/**
 * Created by alari @ 06.04.11 15:27
 */
class I18nBoot {
  static run() {
    I18n.getMetaClass().'static'.invokeMethod = { String code, args ->
      args = args.size() ? args[0] : [:]
      def locale = args.remove("_locale") ?: LCH.getLocale()
      String defaultMessage = args.remove("_default") ?: code
      String encodeAs = args.remove("_encodeAs")
      String text = I18n.messageSource.getMessage(code, args as Object[],
              defaultMessage, locale) ?: defaultMessage

      if (text) {
        return encodeAs ? text."encodeAs${encodeAs}"() : text
      }
      ''
    }
  }
}
