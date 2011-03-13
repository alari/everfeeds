<html>
    <head>
        <title>EverFeeds.com</title>
        <meta name="layout" content="main" />
    </head>
    <body>

    <sec:ifLoggedIn>
            <h1>You are logged in as <sec:username /></h1>
        </sec:ifLoggedIn>
    <g:if test="${flash.message}">
        <h2>${flash.message}</h2>
    </g:if>

            <g:enLogin>Auth With Evernote</g:enLogin>

        <br/><br/>
    <g:gLogin>Auth With Google</g:gLogin>
    <hr/>


    </body>
</html>
