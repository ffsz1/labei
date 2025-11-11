package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

/**
 * @author liuguofu
 *         on 3/26/15.
 */
public interface TicketEnhancer {
    Ticket enhance(Ticket ticket, OAuth2Authentication authentication,AccountDetails userDetails);
}
