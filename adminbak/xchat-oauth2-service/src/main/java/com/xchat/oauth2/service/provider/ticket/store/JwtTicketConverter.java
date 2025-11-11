package com.xchat.oauth2.service.provider.ticket.store;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.provider.ticket.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuguofu
 *         on 3/27/15.
 */
public class JwtTicketConverter implements TicketEnhancer, TicketCoverter,InitializingBean {

    private static final Log logger = LogFactory.getLog(JwtTicketConverter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private String verifierKey = "dh293Hkdjf3G";

    private Signer signer = new MacSigner(verifierKey);

    private String signingKey = verifierKey;

    private SignatureVerifier verifier;


    @Override
    public Map<String, ?> convertTicket(Ticket ticket, OAuth2Authentication authentication, AccountDetails userDetails) {
        Map<String,Object> response = new HashMap<String,Object>();
        response.put(TICKET_ID,ticket.getValue());
        response.put(CLIENT_ID,authentication.getOAuth2Request().getClientId());
        response.put(EXP,ticket.getExpiresIn());
        response.put(USER_ID,userDetails.getAccount().getUid());
        response.put(TICKET_TYPE,ticket.getTicketType());
        return response;
    }
    @Override
    public OAuth2AccessToken extractTicket(String value, Map<String, ?> map) {
        //TODO
        return null;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        //TODO
        return null;
    }

    @Override
    public Ticket enhance(Ticket ticket, OAuth2Authentication authentication,AccountDetails userDetails) {
        DefaultTicket result = new DefaultTicket(ticket);
        result.setValue(encode(ticket,authentication,userDetails));
        return result;
    }

    protected String encode(Ticket ticket, OAuth2Authentication authentication,AccountDetails userDetails) {
        String content;
        try {
            content = objectMapper.writeValueAsString(convertTicket(ticket, authentication,userDetails));
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot convert access token to JSON", e);
        }
        String token = JwtHelper.encode(content, signer).getEncoded();
        return token;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
