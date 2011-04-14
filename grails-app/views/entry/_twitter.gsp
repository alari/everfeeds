<%@ page import="everfeeds.I18n" %>
<img src="${entry.imageUrl}" alt="${entry.author}" style="float:left"/>
<p><a href="http://twitter.com/intent/user?screen_name=${entry.author}">${entry.author}</a></p>
<h4 class="target-blank">${entry.content}</h4>
<p>
    <img src="${resource(dir: "images/twitter/bird_gray", file: "bird_16_gray.png")}" alt="Twitter" width="16" height="16"/>
    <a href="${entry.sourceUrl}" target="_blank"><g:formatDate date="${entry.placedDate}"/></a>

    <a href="http://twitter.com/intent/tweet?in_reply_to=${entry.identity}"><img src="${resource(dir: "images/twitter", file: "reply.png")}" alt="${I18n."entry.twitter.reply"()}" width="16" height="16"/>${I18n."entry.twitter.reply"()}</a>
    <a href="http://twitter.com/intent/retweet?tweet_id=${entry.identity}"><img src="${resource(dir: "images/twitter", file: "retweet.png")}" alt="${I18n."entry.twitter.retweet"()}" width="16" height="16"/>${I18n."entry.twitter.retweet"()}</a>
    <a href="http://twitter.com/intent/favorite?tweet_id=${entry.identity}"><img src="${resource(dir: "images/twitter", file: "favorite.png")}" alt="${I18n."entry.twitter.favorite"()}" width="16" height="16"/>${I18n."entry.twitter.favorite"()}</a>
</p>
<br clear="all"/>