<%@ page import="everfeeds.Access" %>
<div class="entry entry-${entry.type}">
    <g:if test="${entry.type == Access.TYPE_TWITTER}">
        <g:render template="/entry/twitter" model="[entry:entry]"/>
    </g:if>

    <g:if test="${entry.type != Access.TYPE_TWITTER}">
        <g:render template="/entry/default" model="[entry:entry]"/>
    </g:if>
</div>