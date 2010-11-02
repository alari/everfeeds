package com.everfeeds

import com.evernote.edam.notestore.NoteStore
import com.evernote.oauth.consumer.SimpleOAuthRequest
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.springframework.web.context.request.RequestContextHolder
import org.htmlcleaner.*

class EvernoteService {

    static transactional = true

    def grailsApplication

    def getSession(){
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

    NoteStore.Client getNoteStore() {
        String noteStoreUrl = "http://" + grailsApplication.config.evernote.host + "/edam/note/" + session?.evernote?.shardId;
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        new NoteStore.Client(noteStoreProt, noteStoreProt);
    }

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

    String getRequestToken() {
        if (session?.evernote?.requestToken != null) {
            return session.evernote.requestToken
        }
        if (session?.evernote?.accessToken) {
            return null
        }

        // Send an OAuth message to the Provider asking for a new Request
        // Token because we don't have access to the current user's account.
        SimpleOAuthRequest oauthRequestor =
        new SimpleOAuthRequest(grailsApplication.config.evernote.requestTokenUrl,
                grailsApplication.config.evernote.consumer.key,
                grailsApplication.config.evernote.consumer.secret, null);

        Map<String, String> reply = oauthRequestor.sendRequest();

        session.evernote.requestToken = reply.get("oauth_token");
        return session.evernote.requestToken
    }

    void processOauthCallback() {
        // Send an OAuth message to the Provider asking to exchange the
        // existing Request Token for an Access Token
        SimpleOAuthRequest oauthRequestor =
        new SimpleOAuthRequest(grailsApplication.config.evernote.requestTokenUrl,
                grailsApplication.config.evernote.consumer.key,
                grailsApplication.config.evernote.consumer.secret, null);

        oauthRequestor.setParameter("oauth_token", getRequestToken());

        Map<String, String> reply = oauthRequestor.sendRequest();

        session.evernote.accessToken = reply.get("oauth_token");
        session.evernote.shardId = reply.get("edam_shard");
        session.evernote.requestToken = null
    }

    String getOauthUrl(Map args) {
        args.absolute = true

        String oauth_url = grailsApplication.config.evernote.authUrl
        oauth_url += "?oauth_callback=" + g.createLink(args).encodeAsURL()
        oauth_url += "&oauth_token=" + getRequestToken()
        oauth_url
    }
}
