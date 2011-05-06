<%@ page import="everfeeds.I18n" %>
<%--
  @author Dmitry Kurinskiy
  @since 03.05.11 12:08
--%>
<div>
  <g:formRemote name="twitterPush" url="[controller:'push',action:'access']" onSuccess="pushSuccess(data)" onFailure="pushError(textStatus)">
    <g:field name="title" style="width: 70%"/>
    <input type="submit" value="${I18n._."twitter.push.submit"}"/>
    <g:field type="hidden" name="access" value="${access.id}"/>
  </g:formRemote>
</div>