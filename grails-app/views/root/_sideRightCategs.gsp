<script>$("#sideRightBox").html("<ul><g:each in="${categories}" var="c"><li>${c.toString().encodeAsHTML()}</li></g:each></ul>"+
            "<br/><br/>"+
        "<ul><g:each in="${tags}" var="t"><li>${t.toString().encodeAsHTML()}</li></g:each></ul>");</script>