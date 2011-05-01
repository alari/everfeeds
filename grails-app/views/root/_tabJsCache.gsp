<%--
  @author Dmitry Kurinskiy
  @since 18.04.11 16:29
--%>

<script type="text/javascript">
    tabData = {
        <g:if test="${filter.access}">access: ${filter?.access?.id},</g:if>
        wtag: ${filter.withTags*.id.encodeAsJavaScript()},
        wotag: ${filter.withoutTags*.id.encodeAsJavaScript()},
        wcat: ${filter.withCategories*.id.encodeAsJavaScript()},
        wocat: ${filter.withoutCategories*.id.encodeAsJavaScript()},
        wkind: ${filter.withKinds.encodeAsJavaScript()},
        wokind: ${filter.withoutKinds.encodeAsJavaScript()},
        <g:if test="${filter.getNew}">
        newTime: ${System.currentTimeMillis()},
        </g:if>
        listTime: ${filter.splitDate.time},
    };
    cacheTabData();
</script>