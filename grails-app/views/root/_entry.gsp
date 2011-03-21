<div>
            <h2>
                <g:if test="${entry.sourceUrl}">
                    <a href="${entry.sourceUrl}">${entry.title}</a>
                </g:if>
                <g:if test="${!entry.sourceUrl}">
                    ${entry.title}
                </g:if>
            </h2>
            <p><b>Author: ${entry.author}</b></p>
            <p>Date: <g:formatDate date="${entry.placedDate}"/></p>
            <blockquote>${entry.content}</blockquote>
        </div>
        <hr/>