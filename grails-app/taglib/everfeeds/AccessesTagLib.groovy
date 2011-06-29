package everfeeds

import everfeeds.thrift.domain.Access
import everfeeds.thrift.util.Type

class AccessesTagLib {
  def i18n

  def showAccesses = {
    String inner
    Auth.getTypes().each{
      inner = i18n."${it}.title"(null, "html")
      inner = "<img src=\"${resource(dir: 'images/social', file: it.toString() + ".jpg")}\" width='40' height='40' alt='${inner}'/> ${inner}"
      out << "<p>" + link(controller: "access", action: it.toString(), inner) + "</p>"
    }
  }

  def accessPic = {attrs->
    Access access = attrs.access
    out << /<img src="${resource(dir: "images/social", file: Type.getByThrift(access.type).toString() + ".jpg")}" width="14" height="14" hspace="0" vspace="0" border="0" alt="${access.title.encodeAsHTML()}"\/>/
  }
}
