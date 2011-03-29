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
var entriesUrl;

function loadTab(li) {
    var t = $(li).attr("id").split("-");
    var i = t[1];
    t = t[0];
    var c = $(li).attr("class");
    if (!c) {
        if (t == "cat") {
            tabData.wcat.push(i);
        } else if (t == "tag") {
            tabData.wtag.push(i);
        }
        $(li).attr("class", "with");
    } else if (c == "with") {
        if (t == "cat") {
            tabData.wcat.remove(i);
            tabData.wocat.push(i);
        } else if (t == "tag") {
            tabData.wtag.remove(i);
            tabData.wotag.push(i);
        }
        $(li).attr("class", "without");
    } else if (c == "without") {
        if (t == "cat") {
            tabData.wocat.remove(i);
        } else if (t == "tag") {
            tabData.wotag.remove(i);
        }
        $(li).attr("class", "");
    }
    $("#" + tabId).load(entriesUrl, tabData);
}

function aside(rnd) {
    $("#asideBox").html($("#filterAside-" + rnd).html());
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
            if (tabId == "tab-root") {
                $("#asideBox").html("");
            }
        }
    });
});