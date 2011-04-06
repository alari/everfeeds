<%@ page import="everfeeds.I18n" %>
<h3>
        <g:if test="${entry.sourceUrl}">
                    <a href="${entry.sourceUrl}">${entry.title}</a>
                </g:if>
                <g:if test="${!entry.sourceUrl}">
                    ${entry.title}
                </g:if>
        </h3>
        <g:if test="${entry.author}">
        ${entry.author}
            |                        </g:if>
        <g:formatDate date="${entry.placedDate}"/>
            |
        <a href="${entry.sourceUrl}" target="_blank">Link</a>
            |
        ${I18n."entry.tags"}: ${entry.tags*.title.join(", ")}