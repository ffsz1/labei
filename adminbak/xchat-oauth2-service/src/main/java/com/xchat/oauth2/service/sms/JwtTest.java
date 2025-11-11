package com.xchat.oauth2.service.sms;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

/**
 * @author liuguofu
 *         on 3/27/15.
 */
public class JwtTest {

    public static void main(String[] arg) {
        String verifierKey = "123456";
        Signer signer = new MacSigner(verifierKey);
        SignatureVerifier verifier = new MacSigner(verifierKey);

        String ticket = "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjI2LCJ0aWNrZXRfdHlwZSI6bnVsbCwiZXhwIjozNTk5LCJ0aWNrZXRfaWQiOiI5YWY2Y2Y5My04YWM3LTRjMzUtYTUzYi01ZmYzNWJmYmYyZmMiLCJjbGllbnRfaWQiOiJlcmRhcHAtY2xpZW50In0.J008S9tAIMSJd8ZM2v-ZQgf0QoyMjMIIqF0hc6hXBUc";

        Jwt jwt = JwtHelper.decodeAndVerify(ticket, verifier);
        String content = jwt.getClaims();

        System.out.print("content:" + content);

    }
}
