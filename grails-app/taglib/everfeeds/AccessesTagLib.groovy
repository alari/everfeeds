package everfeeds

import everfeeds.access.Manager

class AccessesTagLib {
  def i18n

  def showAccesses = {
    String inner
    Manager.getConfigs().each {type, params ->
      if (params?.auth == false) {
        return
      }
      inner = i18n."${type}.title"(null, "html")
      // FIXME: do not hardcode filetype
      inner = "<img src=\"${resource(dir: 'images/social', file: type + ".jpg")}\" width='40' height='40' alt='${inner}'/> ${inner}"
      out << "<p>" + link(controller: "access", action: type, inner) + "</p>"
    }
  }

  def accessPic = {attrs->
    Access access = attrs.access
    out << /<img src="${resource(dir: "images/social", file: access.type + ".jpg")}" width="14" height="14" hspace="0" vspace="0" border="0" alt="${access.title.encodeAsHTML()}"\/>/
  }
}
