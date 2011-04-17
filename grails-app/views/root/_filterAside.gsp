<%@ page import="everfeeds.I18n" %>
<style type="text/css">
    .with{background:#e0ffff}
    .without{background:#ffc0cb}
</style>
<div class="filterAside" style="display:none">
    <p><b>${I18n._."entry.filter.access"}: ${filter.access.title.encodeAsHTML()}</b></p>

    <p><b>${I18n._."entry.filter.categories"}: <ul>
        <g:each in="${filter.access.categories}" var="category">
            <li
                    class="<g:filterCls obj="${category}" with="${filter.withCategories}" without="${filter.withoutCategories}"/>"
                    id="cat-${category.id}"
                    onclick="loadTab(this)">${category}</li>
        </g:each>
    </ul></b></p>

    <p><b>${I18n._."entry.filter.tags"}: <ul>
        <g:each in="${filter.access.tags}" var="tag">
            <li
                    class="<g:filterCls obj="${tag}" with="${filter.withTags}" without="${filter.withoutTags}"/>"
                    id="tag-${tag.id}"
                    onclick="loadTab(this)">${tag}</li>
        </g:each>
    </ul></b></p>
</div>
<script type="text/javascript">
    tabData = {
        access: ${filter.access.id},
        wtag: ${filter.withTags*.id.encodeAsJavaScript()},
        wotag: ${filter.withoutTags*.id.encodeAsJavaScript()},
        wcat: ${filter.withCategories*.id.encodeAsJavaScript()},
        wocat: ${filter.withoutCategories*.id.encodeAsJavaScript()},
        <g:if test="${filter.getNew}">
        newTime: ${System.currentTimeMillis()},
        </g:if>
        listTime: ${filter.splitDate.time},
    };
    cacheTabData();
</script>