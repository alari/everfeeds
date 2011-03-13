package everfeeds

import org.springframework.web.context.request.RequestContextHolder
import org.htmlcleaner.*

class EvernoteService {

    static transactional = true

    def grailsApplication

    def getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    List ignoreTags = ["APPLET", "BASE", "BASEFONT", "BGSOUND", "BLINK", "BUTTON", "EMBED", "FIELDSET", "FORM",
            "FRAME", "FRAMESET", "HEAD", "IFRAME", "ILAYER", "INPUT", "ISINDEX", "LABEL", "LAYER", "LEGEND", "LINK",
            "MARQUEE", "MENU", "META", "NOFRAMES", "NOSCRIPT", "OBJECT", "OPTGROUP", "OPTION", "PARAM", "PLAINTEXT",
            "SCRIPT", "SELECT", "STYLE", "TEXTAREA", "XML", "DIR"]
    List blockTags = ["BODY", "HTML"]
    List ignoreAttributes = ["id", "class", "onclick", "ondblclick", "accesskey", "data", "dynsrc", "tabindex"]
    // TODO: add all on* attributes

    HtmlCleaner getCleaner() {
        HtmlCleaner cleaner = new HtmlCleaner()
        CleanerTransformations transformations = new CleanerTransformations();

        ignoreTags.each {transformations.addTransformation(new TagTransformation(it.toLowerCase()))}
        blockTags.each {transformations.addTransformation(new TagTransformation(it.toLowerCase(), "div", false))}

        cleaner.setTransformations(transformations)
        cleaner
    }

    String adaptHtmlToEdam(String html) {
        HtmlCleaner cleaner = getCleaner()

        TagNode node = cleaner.clean(html)
        TagNode[] allNodes = node.getAllElements(true)
        allNodes.each { n ->
            ignoreAttributes.each {a ->
                n.removeAttribute(a)
            }
        }
        // TODO: validate via DTD
        // TODO: 5.Validate href and src values to be valid URLs and protocols

        String xml = new BrowserCompactXmlSerializer(cleaner.getProperties()).getXmlAsString(node.children[1])
        // TODO: remove hardcode
        xml.substring(45, xml.size() - 7)
    }


}
