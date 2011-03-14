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

    <sec:ifLoggedIn>
        <table>
            <tr>
                <th width="50%">Actual state</th>
                <th>Local state</th>
            </tr>
            <g:each in="${account.accesses}" var="access">
                <tr>
                    <th colspan="2">${access.identity} (<g:link controller="root" action="sync" params="[id:access.id]">sync</g:link>)</th>
                </tr>
                <tr>
                    <td colspan="2">Categories</td>
                </tr>
                <tr><td>
                    <ul>
                        <g:each in="${access.manager.categories}" var="c"><li>${c}</li></g:each>
                    </ul>
                </td><td>
                    <ul>
                        <g:each in="${access.categories}" var="c"><li>${c}</li></g:each>
                    </ul>
                </td></tr>
                <tr>
                    <td colspan="2">Tags</td>
                </tr>
                <tr><td>
                    <ul>
                        <g:each in="${access.manager.tags}" var="t"><li>${t}</li></g:each>
                    </ul>
                </td><td>
                    <ul>
                        <g:each in="${access.tags}" var="t"><li>${t}</li></g:each>
                    </ul>
                </td></tr>
            </g:each>
        </table>
    </sec:ifLoggedIn>

    </body>
</html>
