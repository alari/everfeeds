<%@ page contentType="text/html;charset=UTF-8" %>
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
                <g:showAccesses/>
            </sec:ifLoggedIn>
		</aside><!-- #sideRight -->

	</section><!-- #middle-->

</body>
</html></g:applyLayout>