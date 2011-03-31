<img src="${entry.imageUrl}" alt="${entry.author}" style="float:left"/>
<p><a href="http://twitter.com/intent/user?screen_name=${entry.author}">${entry.author}</a></p>
        <h4>${entry.content}</h4>
<p>
<img src="${resource(dir:"images/twitter/bird_gray",file:"bird_16_gray.png")}" alt="Twitter" width="16" height="16"/>
<a href="${entry.sourceUrl}" target="_blank"><g:formatDate date="${entry.placedDate}"/></a>
<a href="http://twitter.com/intent/tweet?in_reply_to=${entry.identity}"><img src="${resource(dir:"images/twitter",file:"reply.png")}" alt="Reply" width="16" height="16"/>Reply</a>
<a href="http://twitter.com/intent/retweet?tweet_id=${entry.identity}"><img src="${resource(dir:"images/twitter",file:"retweet.png")}" alt="Reply" width="16" height="16"/>Retweet</a>
<a href="http://twitter.com/intent/favorite?tweet_id=${entry.identity}"><img src="${resource(dir:"images/twitter",file:"favorite.png")}" alt="Reply" width="16" height="16"/>Favorite</a>
</p>
<br clear="all"/>