<%@ page contentType="text/html;charset=UTF-8" %>
<div>
    <g:each in="${entries}" var="entry">
        <g:render template="entry" model="[entry:entry]"/>
    </g:each>
</div>