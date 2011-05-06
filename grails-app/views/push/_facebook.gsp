<%@ page import="everfeeds.I18n" %>
<%--
  @author Boris G. Tsirkin
  @since 05.05.11 15:18
--%>
<div>
  <g:formRemote name="facebookPush" url="[controller:'push',action:'access']" onSuccess="pushSuccess(data)" onFailure="pushError(textStatus)">
    <g:field name="title" style="width: 70%"/>
    <input type="submit" value="${I18n._."facebook.push.submit"}"/>
    <g:field type="hidden" name="access" value="${access.id}"/>
    <g:field type="hidden" name="kind" value="status" />
  </g:formRemote>
</div>