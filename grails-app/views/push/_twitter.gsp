<%--
  @author Dmitry Kurinskiy
  @since 03.05.11 12:08
--%>
<div>
  <g:form controller="root" action="push">
    <g:field name="title" style="width: 70%"/>
    <g:submitToRemote name="sbm" value="Submit status" url="[controller:'root', action:'push']" onSuccess="pushSuccess(data)" onFailure="pushError(textStatus)"/>
    <g:field type="hidden" name="access" value="${access.id}"/>
  </g:form>
  <div id="twitterPush"></div>
</div>