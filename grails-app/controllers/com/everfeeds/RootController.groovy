package com.everfeeds

import com.evernote.oauth.consumer.SimpleOAuthRequest
import org.apache.thrift.transport.THttpClient
import org.apache.thrift.protocol.TBinaryProtocol
import com.evernote.edam.notestore.NoteStore
import com.evernote.edam.type.Notebook

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
        if(session?.evernote?.requestToken == null) {
            redirect action:"index"
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
        log.error ("Callback Request: " + oauthRequestor.encode());
        Map<String, String> reply = oauthRequestor.sendRequest();
        log.error ("Callback Reply: " + reply);
        session.evernote.accessToken = reply.get("oauth_token");
        session.evernote.shardId = reply.get("edam_shard");
        session.evernote.requestToken = null

        redirect action: "list"
    }

    def reset = {
        session.evernote.requestToken = null
        redirect action: "index"
    }

    def list = {
        if(session?.evernote?.accessToken == null) {
              redirect action: "index"
            return
        }

        String resp = ""

        String noteStoreUrl = "http://" + grailsApplication.config.evernote.host + "/edam/note/" + session.evernote.shardId;
          log.error ("Listing notebooks from: " + noteStoreUrl);
          THttpClient noteStoreTrans = new THttpClient(noteStoreUrl);
          TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
          NoteStore.Client noteStore =
            new NoteStore.Client(noteStoreProt, noteStoreProt);
          List<?> notebooks = noteStore.listNotebooks(session.evernote.accessToken);
          for (Object notebook : notebooks) {
            resp += "Notebook: " + ((Notebook)notebook).getName();
          }
        render resp
    }
}
