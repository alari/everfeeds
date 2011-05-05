<%--
  @author Boris G. Tsirkin
  @since 05.05.11 15:18
--%>
<div>
  <g:form controller="root" action="push">
    <g:field name="title" style="width: 70%"/>
    <g:submitToRemote name="sbm" value="Submit status" url="[controller:'root', action:'push']" onSuccess="pushSuccess(data)" onFailure="pushError(textStatus)"/>
    <g:field type="hidden" name="access" value="${access.id}"/>
    <g:field type="hidden" name="kind" value="status" />
  </g:form>
</div>