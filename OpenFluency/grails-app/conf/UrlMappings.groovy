class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/home/index")
        "/help"(view:"/userguide")
        "500"(view:'/error')

        "/"(controller: 'home', action: 'index')
	}
}
