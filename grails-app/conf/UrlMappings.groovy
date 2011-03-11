class UrlMappings {

	static mappings = {
		"/$action?"(controller:"root")
        "/"(controller:"root", action:"index")
        "/oauth/$action"(controller:"oauth")

		"500"(view:'/error')
	}
}
