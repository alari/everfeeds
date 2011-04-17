<%@ page import="everfeeds.I18n" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${I18n._."index.title"}</title>
    <meta name="layout" content="duo"/>
</head>
<body>

<div id="tabss">
    <ul>
        <li><a href="#tab-root">${I18n._."index.tab.root"}</a></li>
        <li><g:link action="entries">${I18n._."index.tab.mash"}</g:link></li>
        <g:each in="${account.accesses}" var="access">
            <li>
                <g:link action="entries" params="[access:access.id]">
                    <img src="${resource(dir: "images/social", file: access.type + ".jpg")}" width="14" height="14" hspace="0" vspace="0" border="0" alt="${access.title.encodeAsHTML()}"/>
                    ${access.title.encodeAsHTML()}</g:link></li>
        </g:each>
    </ul>

    <div id="tab-root">
        <table>
            <tr><td>
                <h2>Useful tips (not i18n until confirmed)</h2>
                <b>
                    <ul>
                        <li>Take a look on mash</li>
                        <li>Filter the feed</li>
                        <li>Create a mix of filtered feeds</li>
                    </ul>
                </b>
            </td><td>
                Connect more social services you use:
                <br/>
                <ul>
                    <li><g:auth type="evernote"><img src="${resource(dir: "images/social", file: "evernote.jpg")}" with="40" height="40" alt="Evernote"/> Evernote</g:auth></li>
                    <li><g:auth type="greader"><img src="${resource(dir: "images/social", file: "greader.jpg")}" with="40" height="40" alt="Google Reader"/> Google Reader</g:auth></li>
                    <li><g:auth type="twitter"><img src="${resource(dir: "images/social", file: "twitter.jpg")}" with="40" height="40" alt="Twitter"/> Twitter</g:auth></li>
                    <li><g:auth type="gmail"><img src="${resource(dir: "images/social", file: "gmail.jpg")}" with="40" height="40" alt="Gmail"/> GMail (inbox/unread)</g:auth></li>
                </ul>

                <hr/>
                Learn how to achieve more with Everfeeds
            </td>
            </tr>
        </table></div></div>

<script type="text/javascript">
entriesUrl = "<g:createLink controller="root" action="entries"/>";
</script>

</body>
</html>
