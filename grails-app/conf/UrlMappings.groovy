class UrlMappings {

  static mappings = {
    "/access/$id/callback"(controller: "access", action: "callback")
    "/access/$id"(controller: "access", action: "auth")

    "/filter/$id"(controller: "root", action: "filter")

    "/$action?"(controller: "root")
    "/"(controller: "root", action: "index")

    "/$controller/$action?/$id?" {}

    "500"(view: '/error')
  }
}
