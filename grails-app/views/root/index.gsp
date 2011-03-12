<html>
    <head>
        <title>EverFeeds.com</title>
        <meta name="layout" content="main" />
    </head>
    <body>
        <sec:ifNotLoggedIn>
            <g:enLogin>Auth With Evernote</g:enLogin>
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
            You are logged in as <sec:username />
        </sec:ifLoggedIn>
    <g:gLogin>Auth With Google</g:gLogin>
    <hr/>


    </body>
</html>
