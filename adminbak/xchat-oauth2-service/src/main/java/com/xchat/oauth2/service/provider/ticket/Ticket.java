package com.xchat.oauth2.service.provider.ticket;

import com.xchat.oauth2.service.common.OAuth2AccessToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author liuguofu
 *         on 3/16/15.
 */
public interface Ticket {

    public static String ONCE_TYPE = "once";

    public static String MULTI_TYPE = "multi";

    /**
     * The access token issued by the authorization server. This value is REQUIRED.
     */
    public static String TICKET = "ticket";

    /**
     * The type of the token issued as described in <a
     * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-7.1">Section 7.1</a>. Value is case insensitive.
     * This value is REQUIRED.
     */
    public static String TICKET_TYPE = "ticket_type";

    /**
     * The lifetime in seconds of the access token. For example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated. This value is OPTIONAL.
     */
    public static String EXPIRES_IN = "expires_in";

    /**
     * The refresh token which can be used to obtain new access tokens using the same authorization grant as described
     * in <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-6">Section 6</a>. This value is OPTIONAL.
     */
    public static String ACCESS_TOKEN = "access_token";


    /**
     * The additionalInformation map is used by the token serializers to export any fields used by extensions of OAuth.
     * @return a map from the field name in the serialized token to the value to be exported. The default serializers
     * make use of Jackson's automatic JSON mapping for Java objects (for the Token Endpoint flows) or implicitly call
     * .toString() on the "value" object (for the implicit flow) as part of the serialization process.
     */
    Map<String, Object> getAdditionalInformation();

    OAuth2AccessToken getAccessToken();

    String getTicketType();

    boolean isExpired();

    Date getExpiration();

    int getExpiresIn();

    String getValue();

    Set<String> getScope();
}
