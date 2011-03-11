<html>
    <head>
        <title>EverFeeds.com</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <a href="${oauth_url}">Auth With Evernote</a>
    <hr/>
    ${g.createLink(action: "callback", absolute: true)}
    <hr/>
    Evernote object: ${session.evernote}
    </body>
</html>
