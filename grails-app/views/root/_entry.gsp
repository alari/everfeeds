<div class="entry entry-${entry.type}">
    <g:if test="${entry.type == 'twitter'}">
        <g:render template="/entry/twitter" model="[entry:entry]"/>
    </g:if>

    <g:if test="${entry.type != 'twitter'}">
        <g:render template="/entry/default" model="[entry:entry]"/>
    </g:if>
</div>