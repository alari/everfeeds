class UrlMappings {

	static mappings = {
		"/$action?"(controller:"root")
        "/"(controller:"root", action:"index")

        "/$controller/$action?/$id?"{

        }

		"500"(view:'/error')
	}
}
