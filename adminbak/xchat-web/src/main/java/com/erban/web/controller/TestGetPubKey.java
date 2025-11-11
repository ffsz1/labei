package com.erban.web.controller;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by liuguofu on 2017/7/7.
 */
public class TestGetPubKey {
    public static void main(String args[])throws Exception{
        String signuatrue="hCjivoCnxSoPBKrtNWhLFJg60kDcegRqODmB16qU7Nkbg/XsmdGkP2KWOBRumn8BIxxIOCXI5rAGwKd+7eFbkHMgNqQQfnxJjJiCeNTyApGfRFIJnndufkXatZi8NyNRvMpWSCb4kCTZQs2X5pU1VeMNpvioWnDEfbome+3DqSB7zoyYUMr7KmWICtdMXewmaTFtnyZCsaP1nVyDX8ZBfe9XDlLCamHIKyQTP9FnpoRvpfW01DVqXDykKEB8Dl3/trP/+nbA7vASMRKITsL7JUCfVaF8j9J09EGy6r9D1WzJLDhpkCSfo4yz6R+rVJG+YxGzjF5HN6sbyZsAZquKgw==";
        String data="{\"id\":\"evt_04qN8cXQvIhssduhS4hpqd9p\",\"created\":1427555016,\"livemode\":false,\"type\":\"account.summary.available\",\"data\":{\"object\":{\"acct_id\":\"acct_0eHSiDyzv9G09ejT\",\"object\":\"account_daily_summary\",\"acct_display_name\":\"xx公司\",\"created\":1425139260,\"summary_from\":1425052800,\"summary_to\":1425139199,\"charges_amount\":1000,\"charges_count\":100}},\"object\":\"event\",\"pending_webhooks\":2,\"request\":null,\"scope\":\"acct_1234567890123456\",\"acct_id\":\"acct_1234567890123456\"}";
        System.out.println(verifyData(data,signuatrue,getPubKey()));
//        getPubKey();
    }
    private static PublicKey getPubKey() throws Exception {
        String pubKeyString ="-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4FxEYbLidyIhfxvlcDsGy6rOk\n" +
                "WH6kN/eFIPyO1vm6/qybYuGp97xVgb6VYg7oYXa1jeGgfDXzgNMZik4bJmGJAjSG\n" +
                "K7yr253WHsfynvAixnZNpsRXvMrfPS+xr9RpHiYMQmTegJAOg7IfKcvzNW7CkTYw\n" +
                "wOUBmQ5GGeX6uGi1IQIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    private static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }
}
