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
		<g:link controller="root">${I18n."layout.title"()}</g:link>
        <sec:ifLoggedIn><g:link controller="logout">${I18n."layout.logout"()}</g:link> </sec:ifLoggedIn>
	</header><!-- #header-->

    <g:if test="${flash.message}">
        <div id="message"><p>${flash.message}</p></div>
        <jq:jquery>$("#message").dialog();</jq:jquery>
    </g:if>

        <g:layoutBody />

</div><!-- #wrapper -->

<footer id="footer">
	&copy; ${I18n."layout.footer.copyright"()}
</footer><!-- #footer -->

</body>
</html>