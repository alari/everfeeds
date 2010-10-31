<html>
    <head>
        <title>EverFeeds.com</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <a href="${oauth_url}">
          Go to EverNote auth
        </a>
    <hr/>
    ${g.createLink(action: "callback", absolute: true)}
    <hr/>
    Evernote object: ${session.evernote}
    </body>
</html>
