<html>
    <head>
        <title>EverFeeds.com access: ${access}</title>
        <meta name="layout" content="main" />
    </head>
    <body>

    <h1>Access: ${access} | <small><g:link action="requestAccessPull" id="${access.id}">Request pull</g:link></small></h1>
    <g:each in="${entries}" var="entry">
        <g:render template="entry" model="[entry:entry]"/>
    </g:each>

    </body>
</html>
