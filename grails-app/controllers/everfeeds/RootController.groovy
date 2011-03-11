package everfeeds

class RootController {


    def index = {

    }
/*
    def list = {
        if (session?.evernote?.accessToken == null) {
            redirect action: "index"
            return
        }

        String resp = ""

        NoteStore.Client noteStore = evernoteService.getNoteStore()
        List<?> notebooks = noteStore.listNotebooks(session.evernote.accessToken);
        for (Object notebook: notebooks) {
            resp += "+Notebook: " + ((Notebook) notebook).getName();
        }
        render resp

        UserStore.Client userStore = evernoteService.getUserStore()
        def user = userStore.getUser(session.evernote.accessToken)
        render "<br/><br/>"
        render user.username
    }



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
