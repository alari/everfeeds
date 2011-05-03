<%@ page contentType="text/html;charset=UTF-8" %>
<div class="entries">
    <g:each in="${entries}" var="entry">
      <g:render template="/entry/envelop" model="[entry:entry]"/>
    </g:each>
</div>