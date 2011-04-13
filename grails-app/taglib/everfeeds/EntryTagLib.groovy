package everfeeds

import everfeeds.envelops.EntryFace

class EntryTagLib {
    def entry = {attrs->
        EntryFace entry = attrs.show

        out << /<div class="entry entry-${entry.type} kind-${entry.kind}">/

        switch(entry){
            case {it.type == "twitter" && it.kind == "DM"}:
                out << render(template:"/entry/twitterDM", model: [entry: entry])
                break;
            case {it.type == "twitter"}:
                out << render(template:"/entry/twitter", model: [entry: entry])
                break;
            default:
            out << render(template:"/entry/default", model: [entry: entry])
        }

        out << "</div>"
    }
}
