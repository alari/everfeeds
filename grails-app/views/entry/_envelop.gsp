<%--
  @author Dmitry Kurinskiy
  @since 03.05.11 13:40
--%>

<div class="entry entry-${entry.type} kind-${entry.kind}">
  <g:render template="${entry.kindClass.template}" model="[entry:entry]"/>
</div>