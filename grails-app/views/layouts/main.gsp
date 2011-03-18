<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="grailsLogo"><g:link controller="root">Everfeeds.com</g:link></div>
        <g:if test="${flash.message}">
        <div id="message">${flash.message}</div>
    </g:if>

        <g:layoutBody />

    <div id="footer">
        &copy; Everfeeds.com, 2011
    </div>
    </body>
</html>