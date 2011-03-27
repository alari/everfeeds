package everfeeds

import grails.plugins.springsecurity.Secured

class RootController {
    def syncService

    def index = {
        if(loggedIn) {
            render view: "authIndex", model: [account: authenticatedUser]
            return
        }
    }

    def accessEntries = {
        Access access = Access.findByIdAndAccount(params.id, authenticatedUser)
        if(!access) {
            render code: 403
            return
        }
        render template: "entries", model: [entries: Entry.findAllByAccess(access, [sort:"placedDate", order: "desc"])]
    }

    def mashEntries = {
        render template: "entries", model: [entries: Entry.findAllByAccount(authenticatedUser, [sort:"placedDate", order: "desc"])]
    }

    //@Secured('ROLE_EVERNOTE')
    def lookAtAccess = {
        Access access = Access.findByIdAndAccount(params.id, authenticatedUser)
        if(!access) {
            flash.message = "Access not found"
            redirect action: "index"
            return;
        }
        [access:access, entries: Entry.findAllByAccess(access, [sort: "placedDate", order: "desc"])]
    }

    //@Secured('IS_AUTHENTICATED_REMEMBERED')
    def requestAccessPull = {
        Access access = Access.findByIdAndAccount(params.id, authenticatedUser)
        if(!access) {
            flash.message = "Access not found"
            redirect action: "index"
            return;
        }
        //syncService.addToQueue access, true
        sendMessage("seda:sync.pull.access", params.id)
        redirect action: "lookAtAccess", id: params.id
    }

    //@Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def mash = {
        [entries: Entry.findAllByAccount(authenticatedUser, [sort: "placedDate", order: "desc"])]
    }


/*
    def rss = {
        String rss_url = "http://vz.ru/columns/rss.xml"


        def rsss = new XmlParser().parse(rss_url)
        rsss.channel.item.each {

            Note note = new Note()
            note.title = it.title.text()



            note.content = """<?xml version="1.0" encoding="UTF-8"?>

    <!DOCTYPE en-note SYSTEM "http://xml.evernote.com/pub/enml2.dtd">

    <en-note>""" + evernoteService.adaptHtmlToEdam(it.description.text()) + "</en-note>"

            render note.title

            evernoteService.getNoteStore().createNote(session.evernote.accessToken, note)
        }
    }    */
}
