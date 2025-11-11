package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.service.UserService;

import java.util.Map;

/**
 * @author liuguofu
 *         on 3/27/15.
 */
public class DefaultTicketConverter implements TicketCoverter
{
    private final UserService userService;

    public DefaultTicketConverter(UserService userService){
        this.userService = userService;
    }

    @Override
    public Map<String, ?> convertTicket(Ticket ticket, OAuth2Authentication authentication,AccountDetails userDetails) {
        return null;
    }

    @Override
    public OAuth2AccessToken extractTicket(String value, Map<String, ?> map) {
        return null;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        return null;
    }
}
