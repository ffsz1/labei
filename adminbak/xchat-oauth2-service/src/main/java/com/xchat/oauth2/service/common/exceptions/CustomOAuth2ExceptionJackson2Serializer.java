package com.xchat.oauth2.service.common.exceptions;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xchat.oauth2.service.common.status.OAuthStatus;
import com.xchat.oauth2.service.core.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author liuguofu
 * on 3/19/15.
 */
public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<OAuth2Exception> {

    public CustomOAuth2ExceptionJackson2Serializer() {
        super(OAuth2Exception.class);
    }

    @Override
    public void serialize(OAuth2Exception value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();

        String errorCode = value.getOAuth2ErrorCode();

        OAuthStatus status;
        String errorMessage;

        switch (errorCode) {
            case OAuth2Exception.INVALID_CLIENT:
                errorMessage = value.getMessage().toLowerCase();
                if (errorMessage.contains("bad") && errorMessage.contains("credentials")) {
                    status = OAuthStatus.CLIENT_SECRET_MISMATCH;
                } else {
                    status = OAuthStatus.INVALID_CLIENT_ID;
                }
                break;
            case "unauthorized":
            case OAuth2Exception.UNAUTHORIZED_CLIENT:
                status = OAuthStatus.UNAUTHORIZED_CLIENT_ID;
                break;
            case OAuth2Exception.INVALID_GRANT:
                errorMessage = value.getMessage().toLowerCase();
                if (errorMessage.contains("redirect") && errorMessage.contains("match")) {
                    status = OAuthStatus.REDIRECT_URI_MISMATCH;
                } else if (errorMessage.contains("bad") && errorMessage.contains("credentials")) {
                    status = OAuthStatus.USERNAME_PASSWORD_MISMATCH;
                } else if (errorMessage.contains("invalid refresh token")) {
                    status = OAuthStatus.INVALID_TOKEN;
                } else {
                    status = OAuthStatus.INVALID_GRANT;
                }
                break;
            case OAuth2Exception.INVALID_SCOPE:
                status = OAuthStatus.INVALID_SCOPE;
                break;
            case OAuth2Exception.INVALID_TOKEN:
                errorMessage = value.getMessage().toLowerCase();
                if (errorMessage.contains("access token expired")) {
                    status = OAuthStatus.ACCESS_TOKEN_HAS_EXPIRED;
                } else {
                    status = OAuthStatus.INVALID_TOKEN;
                }
                break;
            case OAuth2Exception.INVALID_REQUEST:
                status = OAuthStatus.INVALID_REQUEST;
                break;
            case OAuth2Exception.VERSION_ERROR:
                status = OAuthStatus.VERSION_ERROR;
                break;
            case OAuth2Exception.MULTI_ACCOUNT:
                status = OAuthStatus.MULTI_ACCOUNT;
                break;
            //账号封禁处理
            case OAuth2Exception.ACCOUNT_ERROR:
                status = OAuthStatus.ACCOUNT_ERROR;
                break;
            case OAuth2Exception.REDIRECT_URI_MISMATCH:
                status = OAuthStatus.REDIRECT_URI_MISMATCH;
                break;
            case OAuth2Exception.UNSUPPORTED_GRANT_TYPE:
                status = OAuthStatus.UNSUPPORTED_GRANT_TYPE;
                break;
            case OAuth2Exception.UNSUPPORTED_RESPONSE_TYPE:
                status = OAuthStatus.UNSUPPORTED_RESPONSE_TYPE;
                break;
            case OAuth2Exception.ACCESS_DENIED:
                status = OAuthStatus.ACCESS_DENIED;
                break;
            case "unsupported_issue_type":
                status = OAuthStatus.UNSUPPORTED_TICKET_ISSUE_TYPE;
                break;
            case OAuth2Exception.INVALID_USER:
                status = OAuthStatus.INVALID_USER;
                break;
            case OAuth2Exception.THIRD_ACCOUNT_HAS_BIND:
                status = OAuthStatus.THIRD_ACCOUNT_HAVE_BIND;
                break;
            case OAuth2Exception.INVALID_THIRD_TOKEN:
                status = OAuthStatus.INVALID_THIRD_TOKEN;
                break;
            case OAuth2Exception.USER_HAS_SIGNED_UP:
                status = OAuthStatus.USER_HAS_SIGNED_UP;
                break;
            case OAuth2Exception.SERVICE_ERROR:
                status = OAuthStatus.INVALID_SERVICE;
                break;
            case OAuth2Exception.INVALID_IDENTIFYING_CODE:
                status = OAuthStatus.INVALID_IDENTIFYING_CODE;
                break;
            case OAuth2Exception.UNBIND_OPENID_NOT_MATCH:
                status = OAuthStatus.UNBIND_OPENID_NOT_MATCH;
                break;
            case OAuth2Exception.UNBIND_MAIN_ACCOUNT:
                status = OAuthStatus.UNBIND_MAIN_ACCOUNT;
                break;
            case OAuth2Exception.SIGN_IP_TO_OFTEN:
                status = OAuthStatus.SIGN_IP_TO_OFTEN;
                break;
            case OAuth2Exception.SMS_IP_TO_OFTEN:
                status = OAuthStatus.SMS_IP_TO_OFTEN;
                break;
            case OAuth2Exception.DECEIVE_ERROR:
                status = OAuthStatus.DECEIVE_ERROR;
                break;
            default:
                status = OAuthStatus.UNKNOWN;
                break;
        }

        jgen.writeNumberField("code", status.value());
        jgen.writeStringField("message", StringUtils.isBlank(value.getMessage()) ? status.getReasonPhrase() : value.getMessage());
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        jgen.writeEndObject();
    }
}
