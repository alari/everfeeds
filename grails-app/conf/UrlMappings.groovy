class UrlMappings {

	static mappings = {
        "/access/$id"(controller: "access", action: "auth")
        "/access/$id/callback"(controller: "access", action: "callback")

		"/$action?"(controller:"root")
        "/"(controller:"root", action:"index")

        "/$controller/$action?/$id?"{

        }

		"500"(view:'/error')
	}
}
