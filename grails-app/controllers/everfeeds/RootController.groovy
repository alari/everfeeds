package everfeeds

import grails.plugins.springsecurity.Secured

class RootController {


    def index = {
        if(loggedIn) {
            render view: "authIndex", model: [account: authenticatedUser]
            return
        }
    }

    @Secured('IS_AUTHENTICATED_FULLY')
    def lookAtAccess = {
        Access access = Access.findByIdAndAccount(params.id, authenticatedUser)
        [access:access, entries:access.manager.pull()]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def createFeed = {
        Access access = Access.get(params.access)
        if(!access || access.account.id != principal.id) {
            flash.message = "wrong data provided: ${params.access} (${access})"
            redirect action: "index"
            return
        }
        render view: "index", model: [account:authenticatedUser, _feed:[access: access]]
    }

    @Secured('IS_AUTHENTICATED_REMEMBERED')
    def saveFeed = {
        Access access = Access.findByAccountAndId(principal, params.access)
        Category category = Category.findByAccessAndId(access, params.category)
        if(!category) {
            flash.message = "Category not found"
            redirect action: "index"
            return
        }
        access.addToFeeds new Feed(access: access, category: category).save()
        redirect action: "index"
    }

    def sync = {
        if(isLoggedIn()){
            Access.get(params.id)?.manager?.sync()
        }
        redirect action:"index"
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
