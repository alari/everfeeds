<%@ page import="everfeeds.I18n" contentType="text/html;charset=UTF-8" %>
<g:applyLayout name="base">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:layoutHead />
</head>

<body>

	<section id="middle">

		<div id="container">
			<div id="content">

        <g:layoutBody />
			</div><!-- #content-->
		</div><!-- #container-->

		<aside id="sideRight">
            <div id="asideBox"></div>

            <sec:ifLoggedIn>
              <div id="saveFilter">
                <a href="#" onclick="saveFilter('<g:createLink action="save" controller="filter"/>', '${I18n._."filter.create.name"}')">${I18n._."filter.save"}</a>
              </div>
                <g:showAccesses/>
            </sec:ifLoggedIn>
		</aside><!-- #sideRight -->

	</section><!-- #middle-->

</body>
</html></g:applyLayout>