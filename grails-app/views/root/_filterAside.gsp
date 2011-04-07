<%@ page import="everfeeds.I18n" %>
<style type="text/css">
    .with{background:#e0ffff}
    .without{background:#ffc0cb}
</style>
<div class="filterAside" style="display:none">
    <p><b>${I18n."entry.filter.access"()}: ${access.title}</b></p>

    <p><b>${I18n."entry.filter.categories"()}: <ul>
        <g:each in="${access.categories}" var="category">
            <li
                    class="${testClass(category, withCategories, withoutCategories)}"
                    id="cat-${category.id}"
                    onclick="loadTab(this)">${category}</li>
        </g:each>
    </ul></b></p>

    <p><b>${I18n."entry.filter.tags"()}: <ul>
        <g:each in="${access.tags}" var="tag">
            <li
                    class="${testClass(tag, withTags, withoutTags)}"
                    id="tag-${tag.id}"
                    onclick="loadTab(this)">${tag}</li>
        </g:each>
    </ul></b></p>
</div>
<script type="text/javascript">
    tabData = {
        access: ${access.id},
        wtag: ${withTags*.id.encodeAsJavaScript()},
        wotag: ${withoutTags*.id.encodeAsJavaScript()},
        wcat: ${withCategories*.id.encodeAsJavaScript()},
        wocat: ${withoutCategories*.id.encodeAsJavaScript()},
    };
    cacheTabData();
</script>