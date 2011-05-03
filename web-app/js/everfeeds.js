/**
 * Created by alari @ 29.03.11 18:44
 */
Array.prototype.removeItems = function(itemsToRemove) {
    var j;
    for (var i = 0; i < itemsToRemove.length; i++) {
        j = 0;
        while (j < this.length) {
            if (this[j] == itemsToRemove[i]) {
                this.splice(j, 1);
            } else {
                j++;
            }
        }
    }
};
Array.prototype.remove = function(itemToRemove) {
    this.removeItems([itemToRemove]);
};


var tabId = "";
var tabData = {};
var tabDataCache = {};
var entriesUrl;

var aTargetBlank = function(){
    $('.target-blank a').attr("target", "_blank");
};

function pushSuccess(data){
  $(".entries", "#"+tabId).prepend(data);
}

function pushError(string){
  window.status = string;
}

function showFullEntry(a, id){
    var o = $('.entry-content', $(a).parent());
    o.load(entriesUrl, {content: id}, function(){$("a", o).attr('target', '_blank'); o.slideDown();});
}

function cacheTabData(){
    tabDataCache[tabId] = tabData;
}

function pageTab(currPage, a){
    tabData.page = currPage+1;
    $(a).parent().removeClass("load-more").load(entriesUrl, tabData, aTargetBlank);
}

function tabCheckNew(a){
    var data = $.extend({}, tabData);
    var o = $(a).parent();
    o.html(".......");
    data.page = 0;
    data.getNew = true;
    o.removeClass("load-more").load(entriesUrl, data, aTargetBlank);
}

function loadTab(li) {
    var t = $(li).attr("id").split("-");
    var i = t[1];
    t = t[0];
    var c = $(li).attr("class");
    if (!c) {
      switch(t){
        case "cat":
            tabData.wcat.push(i);
          break;
        case "tag":
            tabData.wtag.push(i);
        break;
        case "kind":
            tabData.wkind.push(i);
        break;
      }
        $(li).attr("class", "with");
    } else if (c == "with") {
      switch(t){
        case "cat":
            tabData.wcat.remove(i);
            tabData.wocat.push(i);
          break;
        case "tag":
            tabData.wtag.remove(i);
            tabData.wotag.push(i);
        break;
        case "kind":
            tabData.wkind.remove(i);
            tabData.wokind.push(i);
        break;
      }
        $(li).attr("class", "without");
    } else if (c == "without") {
      switch(t){
        case "cat":
            tabData.wocat.remove(i);
        break;
        case "tag":
            tabData.wotag.remove(i);
        break;
        case "kind":
            tabData.wokind.remove(i);
        break;
      }
        $(li).attr("class", "");
    }
    $("#" + tabId).load(entriesUrl, tabData, aTargetBlank);
}

$(function() {
    $("#tabss").tabs({
        ajaxOptions: {
            error: function(xhr, status, index, anchor) {
                $(anchor.hash).html(
                        "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                                "If this wouldn't be a demo.");
            }
        },
        select: function(event, ui) {
            tabId = ui.panel.id;
            $("#asideBox").html($(".filterAside", ui.panel).html());
            tabData = tabDataCache[tabId];
        },
        load: function(event, ui) {
            tabId = ui.panel.id;
            $("#asideBox").html($(".filterAside", ui.panel).html());
            tabData = tabDataCache[tabId];
            aTargetBlank();
        },
        cache: true
    });
});


// Twitter intents code goes here
(function() {
  var intentRegex = /twitter\.com(\:\d{2,4})?\/intent\/(\w+)/,
      shortIntents = { tweet: true, retweet:true, favorite:true },
      windowOptions = 'scrollbars=yes,resizable=yes,toolbar=no,location=yes',
      winHeight = screen.height,
      winWidth = screen.width;

  function handleIntent(e) {
    e = e || window.event;
    var target = e.target || e.srcElement,
        m, width, height, left, top;

    while (target && target.nodeName.toLowerCase() !== 'a') {
      target = target.parentNode;
    }

    if (target && target.nodeName.toLowerCase() === 'a' && target.href) {
      m = target.href.match(intentRegex);
      if (m) {
        width = 550;
        height = (m[2] in shortIntents) ? 420 : 560;

        left = Math.round((winWidth / 2) - (width / 2));
        top = 0;

        if (winHeight > height) {
          top = Math.round((winHeight / 2) - (height / 2));
        }

        window.open(target.href, 'intent', windowOptions + ',width=' + width + ',height=' + height + ',left=' + left + ',top=' + top);
        e.returnValue = false;
        e.preventDefault && e.preventDefault();
      }
    }
  }

  if (document.addEventListener) {
    document.addEventListener('click', handleIntent, false);
  } else if (document.attachEvent) {
    document.attachEvent('onclick', handleIntent);
  }
}());