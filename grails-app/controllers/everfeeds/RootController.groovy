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
        render template: "sideRightCategs", model: [categories: access.categories, tags: access.tags]
    }

    def tagEntries = {
        Tag tag = Tag.get(params.id)
        Access access = tag?.access
        if(!access?.account?.id == authenticatedUser.id) {
            render code: 403
            return
        }
        def entries = Entry.createCriteria().list {
            tags {
                idEq(Long.parseLong(params.id))
            }
            order "placedDate", "desc"
        }
        log.debug "Entries count: "+entries.size()
        render template: "entries", model: [entries: entries]
    }

    def mashEntries = {
        render template: "entries", model: [entries: Entry.findAllByAccount(authenticatedUser, [sort:"placedDate", order: "desc"])]
        render template: "sideRightCategs", model: "No way out"
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
