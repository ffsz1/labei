package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.common.OAuth2AccessToken;

import java.io.Serializable;
import java.util.*;

/**
 * @author liuguofu
 *         on 3/16/15.
 */
public class DefaultTicket implements Serializable, Ticket{

    private String value;

    private Date expiration;

    private String tokenType = ONCE_TYPE.toLowerCase();

    private OAuth2AccessToken accessToken;

    private Set<String> scope;

    private Map<String, Object> additionalInformation = Collections.emptyMap();
    private String ticketType;

    /**
     * Create an access token from the value provided.
     */
    public DefaultTicket(String value) {
        this.value = value;
    }

    /**
     * Copy constructor for access token.
     *
     * @param ticket
     */
    public DefaultTicket(Ticket ticket) {
        this(ticket.getValue());
        setAdditionalInformation(ticket.getAdditionalInformation());
        setAccessToken(ticket.getAccessToken());
        setExpiration(ticket.getExpiration());
        setScope(ticket.getScope());
        setTicketType(ticket.getTicketType());
    }


    public void setAdditionalInformation(Map<String,Object> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<String, Object>(additionalInformation);
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public String getTicketType() {
        return ticketType;
    }

    @Override
    public boolean isExpired() {
        return expiration != null && expiration.before(new Date());
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    protected void setExpiresIn(int delta) {
        setExpiration(new Date(System.currentTimeMillis() + delta));
    }

    @Override
    public int getExpiresIn() {
        return expiration != null ? Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                .intValue() : 0;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }
}
