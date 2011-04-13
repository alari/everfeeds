<%@ page import="everfeeds.I18n" %>
<img src="${entry.imageUrl}" alt="${entry.author}" style="float:left"/>
<p><a href="http://twitter.com/intent/user?screen_name=${entry.author}">${entry.author}</a></p>
        <h4>${entry.content}</h4>
<p>
<img src="${resource(dir:"images/twitter/bird_gray",file:"bird_16_gray.png")}" alt="Twitter" width="16" height="16"/>
Direct Message
</p>
<br clear="all"/>