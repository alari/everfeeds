<%@ page import="everfeeds.I18n" %>
<%--
  @author Dmitry Kurinskiy
  @since 03.05.11 12:08
--%>
<div>
  <g:formRemote name="addNote" url="[controller:'push', action:'access']" onSuccess="pushSuccess(data)" onFailure="pushError(textStatus)">
    <label for="title"><g:field name="title" style="width: 70%"/></label>
      <br/>
    <textarea name="content" style="width:75%;height:100px"></textarea>
      <br/>
    <input type="submit" value="${I18n._."evernote.push.submit"}"/>
    <g:field type="hidden" name="access" value="${access.id}"/>
  </g:formRemote>
</div>