<html>
    <head>
        <title>EverFeeds.com access: ${access}</title>
        <meta name="layout" content="duo" />
    </head>
    <body>

    <h1>Access: ${access} | <small><g:link action="requestAccessPull" id="${access.id}">Request pull</g:link></small></h1>
    <table>
        <tr>
            <td>

                <g:render template="entries" model="[entries:entries]"/>

            </td>
            <td>
                <ul>
                    <g:each in="${access.categories}" var="c">
                        <li>${c}</li>
                    </g:each>
                </ul>
                <br/>
                <ul>
                    <g:each in="${access.tags}" var="t">
                        <li>${t}</li>
                    </g:each>
                </ul>

            </td>
        </tr>
    </table>


    </body>
</html>
