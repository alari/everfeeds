package everfeeds

import org.springframework.context.i18n.LocaleContextHolder as LCH

/**
 * Created by alari @ 06.04.11 14:40
 *
 * @see everfeeds.bootstrap.I18nBoot
 */
class I18n {

    static I18n get_(){
        SpringUtil.getBean("i18n")
    }

  // Usage: i18n."code(:locale)?"([value, value?, ...]?, "Default message"?, "encode as"?)
  def methodMissing(String code, args) {

      // Defining locale and message code
      def locale = LCH.getLocale()
      // TODO: get locale code from message

      // Preparing things we'll get from args
      String defaultMessage = code
      String encodeAs = null

      args = args as List

      // No arguments given
      if(args.size() == 0) {
        args = []
      } else {
        List params
        // There is a map of args and some unmapped params
        if(args[0] instanceof List) {
          params = args.tail()
          args = args[0]
        } else {
          // Only unmapped params
          params = args
          args = []
        }
        if(params.size()) {
          if(params[0]) defaultMessage = params[0]
          if(params.size() > 1) {
            encodeAs = params[1].toString().toUpperCase()
          }
        }
      }

      String text = SpringUtil.getBean("messageSource").getMessage(code, args as Object[],
              defaultMessage, locale) ?: defaultMessage

      if (text) {
        return encodeAs ? text."encodeAs${encodeAs}"() : text
      }
      ''
  }

    def propertyMissing(String code){
        methodMissing(code, [])
    }
}