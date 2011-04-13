<%@ page contentType="text/html;charset=UTF-8" %>
<div>
    <g:each in="${entries}" var="entry">
        <g:entry show="${entry}"/>
    </g:each>
</div>