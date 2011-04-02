package everfeeds

class AuthTagLib {

    def auth = {attrs, body->
        out << link([controller: "access", action: "auth", id:attrs.type], body())
    }

}
