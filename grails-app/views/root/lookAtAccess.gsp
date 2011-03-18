<html>
    <head>
        <title>EverFeeds.com access: ${access}</title>
        <meta name="layout" content="main" />
    </head>
    <body>

    <h1>Access: ${access}</h1>
    <g:each in="${entries}" var="entry">
        <div>
            <h2>${entry.title}</h2>
            <b>Author: ${entry.author}</b>
            <blockquote>${entry.content}</blockquote>
        </div>
        <hr/>
    </g:each>

    </body>
</html>
