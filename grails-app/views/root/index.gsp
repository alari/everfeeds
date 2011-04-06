<%@ page import="everfeeds.I18n" %>
<html>
    <head>
        <title>${I18n."frontpage.title"()}</title>
        <meta name="layout" content="mono" />
    </head>
    <body>

    <table>
        <tr>
            <td rowspan="2" width="50%">
                ${I18n."frontpage.image"()}
            </td>
            <th>
              ${I18n."frontpage.motto"()}
            </th>
        </tr>
        <tr>
            <td>
                <b>${I18n."frontpage.signup"()}</b>
                <br/>
                <g:showAccesses/>
                <br/>
                <i>${I18n."frontpage.nopassword"()}</i>
            </td>
        </tr>
    </table>

    </body>
</html>