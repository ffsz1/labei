package com.xchat.oauth2.service.provider.endpoint;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/11 16:50
 */
public class AccessTokenVO {
    private long uid;
    private String access_token;
    private String netEaseToken;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String jti;
    private String scope;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getNetEaseToken() {
        return netEaseToken;
    }

    public void setNetEaseToken(String netEaseToken) {
        this.netEaseToken = netEaseToken;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "AccessTokenVO{" +
                "uid=" + uid +
                ", access_token='" + access_token + '\'' +
                ", netEaseToken='" + netEaseToken + '\'' +
                ", token_type='" + token_type + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", jti='" + jti + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
