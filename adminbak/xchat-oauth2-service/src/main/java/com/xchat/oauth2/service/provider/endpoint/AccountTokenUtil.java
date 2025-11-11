package com.xchat.oauth2.service.provider.endpoint;

import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AccountTokenUtil {

    /**
     *
     * @param accessToken
     * @return
     */
    public static AccessTokenVO tokenVO(OAuth2AccessToken accessToken) {
        AccessTokenVO tokenVO = new AccessTokenVO();
        Object jti = accessToken.getAdditionalInformation().get("jti");
        tokenVO.setJti(String.valueOf(jti));
        tokenVO.setAccess_token(accessToken.getValue());
        tokenVO.setExpires_in(String.valueOf(accessToken.getExpiresIn()));
        tokenVO.setNetEaseToken(accessToken.getNetEaseToken());
        tokenVO.setRefresh_token(accessToken.getRefreshToken().getValue());
        tokenVO.setToken_type(accessToken.getTokenType());
        tokenVO.setUid(accessToken.getUid());
        tokenVO.setScope("");
        return tokenVO;
    }

    public static ResponseEntity<BusiResult> getResponseVO(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        AccessTokenVO tokenVO = tokenVO(accessToken);
        return new ResponseEntity<>(new BusiResult(BusiStatus.SUCCESS, tokenVO), headers, HttpStatus.OK);
    }
}
