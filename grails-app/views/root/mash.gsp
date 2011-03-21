<html>
    <head>
        <title>EverFeeds.com MASH</title>
        <meta name="layout" content="main" />
    </head>
    <body>

  <g:each in="${entries}" var="entry">
        <g:render template="entry" model="[entry:entry]"/>
    </g:each>

    </body>
</html>
