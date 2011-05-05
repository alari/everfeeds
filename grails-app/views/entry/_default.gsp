<%@ page import="everfeeds.I18n" %>
<h3>
    <g:if test="${entry.sourceUrl}">
        <a href="${entry.sourceUrl}" target="_blank">${entry.title}</a>
    </g:if>
    <g:if test="${!entry.sourceUrl}">
        ${entry.title}
    </g:if>
</h3>

<g:if test="${entry.imageUrl}">
  <img src="${entry.imageUrl}" alt="${entry.title.encodeAsHTML()}"/>
</g:if>

<g:if test="${entry.author}">
    <span title="${entry.authorIdentity.encodeAsHTML()}">${entry.author.encodeAsHTML()}</span>
    |</g:if>
<g:formatDate date="${entry.placedDate}"/>
|

<g:if test="${entry.sourceUrl}">
    <a href="${entry.sourceUrl}" target="_blank">${I18n._."entry.link"}</a>
    |
</g:if>

<g:if test="${entry.tags.size()}">
    ${I18n._."entry.tags"}: ${entry.tags.join(", ")}
</g:if>

<g:if test="${entry.content?.size() || entry.description?.size()}">
<div class="entry-content target-blank">
  <g:if test="${entry.description}">
  ${entry.description}
  </g:if>
  <g:if test="${!entry.description}">
    <a href="#" onclick="showFullEntry($(this).parent(), '${entry.id}')">${I18n._."entry.fulltext"}</a>
  </g:if>
</div>
</g:if>