package everfeeds.access.google

import everfeeds.access.AOAuthAuth
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.scribe.model.Token

/**
 * Created by alari @ 02.04.11 12:59
 */
abstract class GoogleAuth extends AOAuthAuth {
    static public Map authCallback(Token accessToken) {
        String email = callApi(
                ConfigurationHolder.config.access.google.oauth,
                ConfigurationHolder.config.access.google.emailUrl.toString(),
                accessToken.token, accessToken.secret
        )

        if (email) {
            email = email.substring(email.indexOf("=") + 1, email.indexOf("&"))
        } else {
            return null
        }

        if (!email) return null

        [
                screen: email
        ]
    }
}
