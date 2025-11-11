package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.OAuth2RefreshToken;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;

import java.util.Collection;

/**
 * @author liuguofu
 *         on 3/16/15.
 */
public interface TicketStore {
    void storeTicket(Ticket ticket,OAuth2Authentication authentication,AccountDetails userDetails);
    Ticket readTicket(String ticketValue);

    public Ticket getTicketCacheByUid(Long uid);
    public String getAccessTokenCache(Long uid);

    void removeTicket(Ticket ticket);
    void removeTicketUsingAccessToken(OAuth2AccessToken accessToken);
    void removeTicketUsingRefreshToken(OAuth2RefreshToken refreshToken);
    Collection<Ticket> findTicketsByAccessToken(OAuth2AccessToken accessToken);
    Collection<Ticket> findTicketsByRefreshToken(OAuth2RefreshToken refreshToken);

}
