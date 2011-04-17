<%@ page import="everfeeds.I18n" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>${I18n._."frontpage.title"}</title>
        <meta name="layout" content="mono" />
    </head>
    <body>

    <table>
        <tr>
            <td rowspan="2" width="50%">
                ${I18n._."frontpage.image"}
            </td>
            <th>
              ${I18n._."frontpage.motto"}
            </th>
        </tr>
        <tr>
            <td>
                <b>${I18n._."frontpage.signup"}</b>
                <br/>
                <g:showAccesses/>
                <br/>
                <i>${I18n._."frontpage.nopassword"}</i>
            </td>
        </tr>
    </table>

    </body>
</html>