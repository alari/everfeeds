<%@ page import="everfeeds.Filter" %>
<%--
  @author Dmitry Kurinskiy
  @since 18.04.11 16:29
--%>

<script type="text/javascript">
    tabData = ${filter.asJavascript()};
    ${filter instanceof Filter ? /tabData.entriesUrl = "/+g.createLink(controller: "filter", action: "entries", id: filter.id)+/"/ : ""}
    cacheTabData();
</script>