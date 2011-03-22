<html>
    <head>
        <title>EverFeeds.com private area</title>
        <meta name="layout" content="main" />
    </head>
    <body>

   <table>
       <tr>
           <td>
               <b>Available accesses</b>
               <div style="height:500px;scroll:auto">
                   <ul>
                    <g:each in="${account.accesses}" var="access">
                        <li><g:link action="lookAtAccess" id="${access.id}"><img src="${resource(dir:"images/social", file:access.type+".jpg")}" with="40" height="40" alt="${access.type}"/> (${access.title})</g:link></li>
                    </g:each>
                   </ul>
               </div>
           </td><td>
            <h2>Useful tips</h2>
           <b>
           <ul>
               <li><g:link action="mash">Take a look on mash</g:link></li>
               <li>Filter the feed and transfer it to another social service</li>
               <li>Create a mix of filtered feeds</li>
           </ul>
               </b>
           </td><td>
           Connect more social services you use:
                <br/>
                <ul>
                    <li><g:auth type="evernote"><img src="${resource(dir:"images/social", file:"evernote.jpg")}" with="40" height="40" alt="Evernote"/> Evernote</g:auth></li>
                    <li><g:auth type="greader"><img src="${resource(dir:"images/social", file:"greader.jpg")}" with="40" height="40" alt="Google Reader"/> Google Reader</g:auth></li>
                    <li><g:auth type="twitter"><img src="${resource(dir:"images/social", file:"twitter.jpg")}" with="40" height="40" alt="Twitter"/> Twitter</g:auth></li>
                </ul>

           <hr/>
           Learn how to achieve more with Everfeeds
           </td>
       </tr>
   </table>

    <sec:ifNotLoggedIn>
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
        <g:form action="createFeed">
            <fieldset><legend>Create feed</legend>
                Access: <g:select from="${account.accesses}" name="access" optionKey="id"></g:select>
                <g:submitButton name="submit" value="Create"/>
            </fieldset>
        </g:form>
        <g:if test="${_feed}">
            <g:form action="saveFeed">
                <fieldset>
                    <legend>Save feed</legend>
                    Category: <g:select from="${_feed.access.categories}" name="category" optionKey="id"></g:select>
                    <g:submitButton name="submit" value="Save"/>
                    <g:field type="hidden" name="access" value="${_feed.access.id}"/>
                </fieldset>
            </g:form>
        </g:if>
        <g:each in="${account.feeds}" var="feed">
            ${feed} <br/>
        </g:each>
    </sec:ifNotLoggedIn>

    </body>
</html>
