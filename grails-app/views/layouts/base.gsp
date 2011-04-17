<%@ page import="everfeeds.I18n" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<!--[if IE]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
	<title><g:layoutTitle default="Everfeeds.com" /></title>
	<!--[if lte IE 6]><link rel="stylesheet" href="${resource(dir:'css',file:'ie.css')}" type="text/css" media="screen, projection" /><![endif]-->
    <link rel="stylesheet" href="${resource(dir:'css',file:'base.css')}" type="text/css" media="screen, projection" />
    <g:layoutHead />
    <g:javascript library="jquery" />
    <g:javascript library="everfeeds"/>
    <jqui:resources/>
</head>

<body>

<div id="wrapper">

	<header id="header">
		<g:link controller="root">${I18n._."layout.title"}</g:link>
        <sec:ifLoggedIn><g:link controller="logout">${I18n._."layout.logout"}</g:link> </sec:ifLoggedIn>
	</header><!-- #header-->

    <g:if test="${flash.message}">
        <div id="message"><p>${flash.message}</p></div>
        <jq:jquery>$("#message").dialog();</jq:jquery>
    </g:if>
    <g:if test="${flash.error}">
        <div id="error"><p>${flash.error}</p></div>
        <jq:jquery>$("#error").dialog();</jq:jquery>
    </g:if>

        <g:layoutBody />

</div><!-- #wrapper -->

<footer id="footer">
	&copy; ${I18n._."layout.footer.copyright"}
</footer><!-- #footer -->

<script type="text/javascript">
reformal_wdg_domain    = "everfeeds";
reformal_wdg_mode    = 0;
reformal_wdg_title   = "Everfeeds.com";
reformal_wdg_ltitle  = "";
reformal_wdg_lfont   = "";
reformal_wdg_lsize   = "";
reformal_wdg_color   = "#000000";
reformal_wdg_bcolor  = "#516683";
reformal_wdg_tcolor  = "#FFFFFF";
reformal_wdg_align   = "left";
reformal_wdg_waction = 0;
reformal_wdg_vcolor  = "#9FCE54";
reformal_wdg_cmline  = "#E0E0E0";
reformal_wdg_glcolor  = "#105895";
reformal_wdg_tbcolor  = "#FFFFFF";

reformal_wdg_bimage = "http://idea.informer.com/files/images/buttons/8489db229aa0a66ab6b80ebbe0bb26cd.png";

</script>
<script type="text/javascript" language="JavaScript" src="http://idea.informer.com/tab6.js?domain=everfeeds"></script>

</body>
</html>