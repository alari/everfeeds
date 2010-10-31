package com.everfeeds

import com.evernote.edam.notestore.NoteStore
import com.evernote.edam.type.Note
import com.evernote.edam.type.Notebook
import com.evernote.oauth.consumer.SimpleOAuthRequest
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.THttpClient
import org.htmlcleaner.*

class RootController {

    def index = {
        if (session.evernote == null) session.evernote = [:]

        if (!session?.evernote?.requestToken && !session?.evernote?.accessToken) {
            // Send an OAuth message to the Provider asking for a new Request
            // Token because we don't have access to the current user's account.
            SimpleOAuthRequest oauthRequestor =
            new SimpleOAuthRequest(grailsApplication.config.evernote.requestTokenUrl,
                    grailsApplication.config.evernote.consumer.key,
                    grailsApplication.config.evernote.consumer.secret, null);
            log.error "Request: " + oauthRequestor.encode();
            Map<String, String> reply = oauthRequestor.sendRequest();
            log.error "Reply: " + reply;
            session.evernote.requestToken = reply.get("oauth_token");
        }
        String oauth_url = grailsApplication.config.evernote.authUrl
        oauth_url += "?oauth_callback=" + g.createLink(controller: "root", action: "callback", absolute: true).encodeAsURL()
        oauth_url += "&oauth_token=" + session.evernote.requestToken
        [oauth_url: oauth_url]
    }

    def callback = {
        if (session?.evernote?.requestToken == null) {
            redirect action: "index"
            return
        }
        // Send an OAuth message to the Provider asking to exchange the
        // existing Request Token for an Access Token
        SimpleOAuthRequest oauthRequestor =
        new SimpleOAuthRequest(grailsApplication.config.evernote.requestTokenUrl,
                grailsApplication.config.evernote.consumer.key,
                grailsApplication.config.evernote.consumer.secret, null);
        log.error session.evernote
        oauthRequestor.setParameter("oauth_token", session.evernote.requestToken);
        log.error("Callback Request: " + oauthRequestor.encode());
        Map<String, String> reply = oauthRequestor.sendRequest();
        log.error("Callback Reply: " + reply);
        session.evernote.accessToken = reply.get("oauth_token");
        session.evernote.shardId = reply.get("edam_shard");
        session.evernote.requestToken = null

        redirect action: "list"
    }

    def reset = {
        session?.evernote?.requestToken = null
        redirect action: "index"
    }

    def list = {
        if (session?.evernote?.accessToken == null) {
            redirect action: "index"
            return
        }

        String resp = ""

        String noteStoreUrl = "http://" + grailsApplication.config.evernote.host + "/edam/note/" + session.evernote.shardId;
        log.error("Listing notebooks from: " + noteStoreUrl);
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        NoteStore.Client noteStore =
        new NoteStore.Client(noteStoreProt, noteStoreProt);
        List<?> notebooks = noteStore.listNotebooks(session.evernote.accessToken);
        for (Object notebook: notebooks) {
            resp += "Notebook: " + ((Notebook) notebook).getName();
        }
        render resp
    }

    NoteStore.Client getNoteStore() {
        String noteStoreUrl = "http://" + grailsApplication.config.evernote.host + "/edam/note/" + session.evernote.shardId;
        THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
        TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
        new NoteStore.Client(noteStoreProt, noteStoreProt);
    }

    HtmlCleaner getCleaner() {
        HtmlCleaner cleaner = new HtmlCleaner()

        CleanerTransformations transformations = new CleanerTransformations();

        List ignoreTags = ["APPLET", "BASE", "BASEFONT", "BGSOUND", "BLINK", "BUTTON", "EMBED", "FIELDSET", "FORM",
                "FRAME", "FRAMESET", "HEAD", "IFRAME", "ILAYER", "INPUT", "ISINDEX", "LABEL", "LAYER", "LEGEND", "LINK",
                "MARQUEE", "MENU", "META", "NOFRAMES", "NOSCRIPT", "OBJECT", "OPTGROUP", "OPTION", "PARAM", "PLAINTEXT",
                "SCRIPT", "SELECT", "STYLE", "TEXTAREA", "XML", "DIR"]
        List blockTags = ["BODY", "HTML"]

        ignoreTags.each {transformations.addTransformation(new TagTransformation(it.toLowerCase()))}
        blockTags.each {transformations.addTransformation(new TagTransformation(it.toLowerCase(), "div", false))}

        cleaner.setTransformations(transformations)
        cleaner
    }

    String adoptHtmlToEdam(String html) {
        HtmlCleaner cleaner = getCleaner()
        List ignoreAttributes = ["id", "class", "onclick", "ondblclick", "accesskey", "data", "dynsrc", "tabindex"]

        TagNode node = cleaner.clean(html)
        TagNode[] allNodes = node.getAllElements(true)
        allNodes.each { n ->
            ignoreAttributes.each {a ->
                n.removeAttribute(a)
            }
        }

        String xml = new BrowserCompactXmlSerializer(cleaner.getProperties()).getXmlAsString(node.children[1])
        xml.substring(45, xml.size()-7)
    }

    def rss = {
        String rss_url = "http://vz.ru/columns/rss.xml"


        def rsss = new XmlParser().parse(rss_url)
        rsss.channel.item.each {

            Note note = new Note()
            note.title = it.title.text()



            note.content = """<?xml version="1.0" encoding="UTF-8"?>

    <!DOCTYPE en-note SYSTEM "http://xml.evernote.com/pub/enml2.dtd">

    <en-note>""" + adoptHtmlToEdam(it.description.text()) + "</en-note>"

            render note.title

            try {
                getNoteStore().createNote(session.evernote.accessToken, note)
            } catch(Exception e) {
                log.error e
            }
        }
    }
}
