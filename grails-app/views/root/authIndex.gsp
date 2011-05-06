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
        <g:each in="${tabsAccesses}" var="access">
            <li>
                <g:link action="entries" params="[access:access.id]">
                    <g:accessPic access="${access}"/>
                    ${access.title.encodeAsHTML()}</g:link></li>
        </g:each>
      <g:each in="${filters}" var="filter">
        <li>
                <g:link action="entries" controller="filter" id="${filter.id}">
                    ${filter.title.encodeAsHTML()}</g:link></li>
      </g:each>
    </ul>

    <div id="tab-root">
      <div>
        <g:formRemote name="absolutePush" url="[controller:'push',action:'absolute']">
          <g:each in="${pushAccesses}" var="access">
            <g:accessPic access="${access}"/> ${access} <br/>
          </g:each>
        </g:formRemote>
      </div>
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
              <b>Accesses you connect:</b>
              <ul>
                <g:each in="${account.accesses}" var="access">
                  <li><g:auth type="${access.type}"><img src="${resource(dir: "images/social", file: access.type + ".jpg")}" width="14" height="14" hspace="0" vspace="0" border="0" alt="${access.title.encodeAsHTML()}"/>
                    ${access.title.encodeAsHTML()}</g:auth></li>
                </g:each>
              </ul>
              <hr/>

              <g:if test="${expiredAccesses.size()}">
                <b>Please revalidate expired accesses:</b>
                <ul>
                  <g:each in="${expiredAccesses}" var="access">
                    <li><g:auth type="${access.type}">${access.title}</g:auth></li>
                  </g:each>
                </ul>
                <hr/>
              </g:if>

                Connect more social services you use:
                <br/>
                <ul>
                    <li><g:auth type="evernote"><img src="${resource(dir: "images/social", file: "evernote.jpg")}" with="40" height="40" alt="Evernote"/> Evernote</g:auth></li>
                    <li><g:auth type="greader"><img src="${resource(dir: "images/social", file: "greader.jpg")}" with="40" height="40" alt="Google Reader"/> Google Reader</g:auth></li>
                    <li><g:auth type="twitter"><img src="${resource(dir: "images/social", file: "twitter.jpg")}" with="40" height="40" alt="Twitter"/> Twitter</g:auth></li>
                    <li><g:auth type="gmail"><img src="${resource(dir: "images/social", file: "gmail.jpg")}" with="40" height="40" alt="Gmail"/> GMail (inbox/unread)</g:auth></li>
                    <li><g:auth type="vkontakte">vkontakte</g:auth></li>
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
