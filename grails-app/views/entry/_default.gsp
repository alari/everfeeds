<%@ page import="everfeeds.I18n" %>
<h3>
    <g:if test="${entry.sourceUrl}">
        <a href="${entry.sourceUrl}" target="_blank">${entry.title}</a>
    </g:if>
    <g:if test="${!entry.sourceUrl}">
        ${entry.title}
    </g:if>
</h3>
<g:if test="${entry.author}">
    ${entry.author}
    |</g:if>
<g:formatDate date="${entry.placedDate}"/>
|

<g:if test="${entry.sourceUrl}">
    <a href="${entry.sourceUrl}" target="_blank">${I18n."entry.link"()}</a>
    |
</g:if>

<g:if test="${entry.tags.size()}">
    ${I18n."entry.tags"()}: ${entry.tags.join(", ")}
    |
</g:if>
<a href="javascript:void(0)" onclick="showFullEntry(this, ${entry.id})">${I18n."entry.fulltext"()}</a>

<div class="entry-content">
</div>