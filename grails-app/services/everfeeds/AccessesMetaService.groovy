package everfeeds

class AccessesMetaService {

    static transactional = true

    static final TYPES = [
            TWITTER: [name: "twitter",title:"Twitter"],
            EVERNOTE: [name:"evernote", title:"Evernote"],
            GMAIL: [name:"gmail",title:"Gmail (inbox/unread)"],
            GREADER: [name:"greader", title:"Google Reader"],
    ]

    def serviceMethod() {

    }
}
