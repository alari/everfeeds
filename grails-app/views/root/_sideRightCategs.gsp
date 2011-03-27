<script>$("#sideRightBox").html("<ul><g:each in="${categories}" var="c"><li>${c.title}</li></g:each></ul>"+
            "<br/><br/>"+
        '<ul><g:each in="${tags}" var="t"><li><g:link controller="root" action="tagEntries" id="${t.id}" onclick="loadTab(this);return false;">${t.title}</g:link></li></g:each></ul>');</script>