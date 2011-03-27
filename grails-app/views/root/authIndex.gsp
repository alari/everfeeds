<html>
    <head>
        <title>EverFeeds.com private area</title>
        <meta name="layout" content="duo" />
    </head>
    <body>

    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Root</a></li>
            <li><g:link action="mashEntries">Mash</g:link></li>
            <g:each in="${account.accesses}" var="access">
                <li><g:link action="accessEntries" id="${access.id}">${access}</g:link></li>
            </g:each>
        </ul>

    <div id="tabs-1">
   <table>
       <tr>
           <td>
               <b>Available accesses</b>
               <div style="max-height:400px;scroll:auto">
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
   </table>     </div>  </div>

    <jq:jquery>
		$( "#tabs" ).tabs({
		    collapsible: true,
			ajaxOptions: {
				error: function( xhr, status, index, anchor ) {
					$( anchor.hash ).html(
						"Couldn't load this tab. We'll try to fix this as soon as possible. " +
						"If this wouldn't be a demo." );
				}
			}
		});
	</jq:jquery>
    </body>
</html>
