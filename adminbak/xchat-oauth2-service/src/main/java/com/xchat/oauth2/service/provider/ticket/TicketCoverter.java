package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

import java.util.Map;

/**
 * @author liuguofu
 *         on 3/26/15.
 */
public interface TicketCoverter {

    final String TICKET_ID = "ticket_id";

    final String TICKET_TYPE = "ticket_type";

    final String CLIENT_ID = "client_id";

    final String EXP = "exp";

    final String JTI = "jti";

    final String ATI = "ati";

    final String SCOPE = OAuth2AccessToken.SCOPE;

    final String AUTHORITIES = "authorities";

    final String USER_ID = "uid";

    /**
     * @param ticket an ticket
     * @param authentication the current OAuth authentication
     *
     * @return a map representation of the token suitable for a JSON response
     *
     */
    Map<String, ?> convertTicket(Ticket ticket, OAuth2Authentication authentication,AccountDetails userDetails);

    /**
     * Recover an access token from the converted value. Half the inverse of
     * {@link #convertTicket(com.xchat.oauth2.service.common.OAuth2AccessToken, com.xchat.oauth2.service.provider.OAuth2Authentication)}.
     *
     * @param value the token value
     * @param map information decoded from an access token
     * @return an access token
     */
    OAuth2AccessToken extractTicket(String value, Map<String, ?> map);

    /**
     * Recover an {@link com.xchat.oauth2.service.provider.OAuth2Authentication} from the converted access token. Half the inverse of
     * {@link #convertAccessToken(com.xchat.oauth2.service.common.OAuth2AccessToken, com.xchat.oauth2.service.provider.OAuth2Authentication)}.
     *
     * @param map information decoded from an access token
     * @return an authentication representing the client and user (if there is one)
     */
    OAuth2Authentication extractAuthentication(Map<String, ?> map);
}
