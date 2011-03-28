<script>$("#sideRightBox").html("<ul><g:each in="${categories}" var="c"><li>${c.id}:${c.title}</li></g:each></ul>"+
            "<br/><br/>"+
        '<ul><g:each in="${tags}" var="t"><li><a href="#" onclick="loadTab(this);return false;">${t.id}:${t.title}</a></li></g:each></ul>');</script>