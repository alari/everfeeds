<%@ page import="everfeeds.I18n" %>
<img src="${entry.imageUrl}" alt="${entry.author}" style="float:left"/>
<p><a href="http://twitter.com/intent/user?user_id=${entry.authorIdentity}">${entry.author}</a></p>
<h4 class="target-blank">${entry.content}</h4>
<p>
    <img src="${resource(dir: "images/twitter/bird_gray", file: "bird_16_gray.png")}" alt="Twitter" width="16" height="16"/>
    Direct Message
</p>
<br clear="all"/>