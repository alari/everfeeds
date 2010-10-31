class UrlMappings {

	static mappings = {
		"/$action?"(controller:"root")
        "/"(controller:"root", action:"index")

		"500"(view:'/error')
	}
}
