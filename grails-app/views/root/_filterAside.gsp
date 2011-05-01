<%@ page import="everfeeds.I18n" %>
<style type="text/css">
    .with{background:#e0ffff}
    .without{background:#ffc0cb}
</style>
<div class="filterAside" style="display:none">
    <p><b>${I18n._."entry.filter.access"}: ${filter.access.title.encodeAsHTML()}</b></p>

  <g:if test="${filter.access.categories.size() > 1}">
    <p><b>${I18n._."entry.filter.categories"}: <ul>
        <g:each in="${filter.access.categories}" var="category">
            <li
                    class="<g:filterCls obj="${category}" with="${filter.withCategories}" without="${filter.withoutCategories}"/>"
                    id="cat-${category.id}"
                    onclick="loadTab(this)">${category}</li>
        </g:each>
    </ul></b></p>
    </g:if>

  <g:if test="${filter.access.tags.size()}">
    <p><b>${I18n._."entry.filter.tags"}: <ul>
        <g:each in="${filter.access.tags}" var="tag">
            <li
                    class="<g:filterCls obj="${tag}" with="${filter.withTags}" without="${filter.withoutTags}"/>"
                    id="tag-${tag.id}"
                    onclick="loadTab(this)">${tag}</li>
        </g:each>
    </ul></b></p>
    </g:if>

  <g:if test="${filter.access.accessor.kinds.size() > 1}">
      <p><b>${I18n._."entry.filter.kinds"}: <ul>
        <g:each in="${filter.access.accessor.kinds}" var="kind">
            <li
                    class="<g:filterCls obj="${kind}" with="${filter.withKinds}" without="${filter.withoutKinds}"/>"
                    id="kind-${kind}"
                    onclick="loadTab(this)">${I18n._.m('kind.'+kind)}</li>
        </g:each>
    </ul></b></p>
    </g:if>
</div>