package everfeeds.bootstrap

import org.springframework.context.i18n.LocaleContextHolder as LCH

import everfeeds.I18n

/**
 * Created by alari @ 06.04.11 15:27
 */
class I18nBoot {
  static run() {
    // Usage: I18n."code(:locale)?"(param:value?, param:value?, "Default message"?, "encode as"?)
    I18n.metaClass.'static'.invokeMethod = { String code, args ->

      // Defining locale and message code
      def locale = LCH.getLocale()
      // TODO: get locale code from message

      // Preparing things we'll get from args
      String defaultMessage = code
      String encodeAs = null

      args = args as List

      // No arguments given
      if(args.size() == 0) {
        args = [:]
      } else {
        List params
        // There is a map of args and some unmapped params
        if(args[0] instanceof Map) {
          params = args.tail()
          args = args[0]
        } else {
          // Only unmapped params
          params = args
          args = [:]
        }
        if(params.size()) {
          if(params[0]) defaultMessage = params[0]
          if(params.size() > 1) {
            encodeAs = params[1].toString().toUpperCase()
          }
        }
      }

      String text = I18n.messageSource.getMessage(code, args as Object[],
              defaultMessage, locale) ?: defaultMessage

      if (text) {
        return encodeAs ? text."encodeAs${encodeAs}"() : text
      }
      ''
    }
  }
}
