package everfeeds

import everfeeds.envelops.EntryFace

class EntryTagLib {
    def entry = {attrs->
        EntryFace entry = attrs.show

        out << /<div class="entry entry-${entry.type} kind-${entry.kind}">/

      out << render(template: entry.kindClass.template, model: [entry: entry])

        out << "</div>"
    }
}
