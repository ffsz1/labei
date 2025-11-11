package com.erban.web.test;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySecurityRiskRainscoreQueryRequest;
import com.alipay.api.response.AlipaySecurityRiskRainscoreQueryResponse;

/**
 * Created by liuguofu on 2017/10/24.
 */
public class Test {
    public static void main(String args[]) throws Exception{
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","2017070307631076","MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMP7SW5zh6CUUvhE\n" +
                "rCRxifJlnXgWySPCLtR75Bc3wHFd28wXyLcqIYdtB0Dsw0sMk+990zsHK2AhBO6z\n" +
                "oEyXfqFWHimCzZGfwy8o2lu/ZBM4MSMGPdGqm31oJCQ0r9WiMfpqM5YPAYW6awhn\n" +
                "ITcAmAv8WGCSS3vfOTJZjqt5lWqXAgMBAAECgYA3hpKfgIwEqwF25b3/9UsoKjIb\n" +
                "PBlw0CuEeDV/foQ/mmATywlLG9y0CefsNSh/suiXCgc2yik36yMCnlHkYWe2vmko\n" +
                "zsYDtuyHOyxXNwbTv9r6qdT4iORLnFABp9atdA2vhHrGRUzowpWOoHQSg3tXrgNJ\n" +
                "WrkGUeJUeCMk9BspQQJBAPwAkVo4cm8mJ4ATdvziMG7iip6JeIXapw73MD+bhDhI\n" +
                "yO73leXjcGdm1zUyGVfydaphaWP85jrNodR8urKL0IUCQQDHFzU5CHVf0TnZQixL\n" +
                "2r9J7mGhnZgnHPvXTkpuu5DoRLDYSy8JMqAV0xfaptSJrs7+igAkdSyZYakVA2vh\n" +
                "LSdrAkBalMWqfWG8zCUkp1p82bz5DpsvE1DsJEUqXvXL11W2eYsoQnxyDsfHkzLG\n" +
                "H0T21OMwpCUi2LPU7Tc/Tro5FYKVAkARVwgfq4jti3+KWKUbBjZa7hQ6y0sRPQkC\n" +
                "O6Nn+ZYpZLIyZHn4RxyCShxtwtIIG92TddmFU19LDKJ5Xg7rUc95AkAoonu839y5\n" +
                "Sst42rTuGd3onMP1Ajrgne16gjYhYyv/MadR5wCnY4KNC36W2z/FOFQEgAxCQnug\n" +
                "HbdoN6chMvxv","json","GBK","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD+0luc4eglFL4RKwkcYnyZZ14\n" +
                "Fskjwi7Ue+QXN8BxXdvMF8i3KiGHbQdA7MNLDJPvfdM7BytgIQTus6BMl36hVh4p\n" +
                "gs2Rn8MvKNpbv2QTODEjBj3Rqpt9aCQkNK/VojH6ajOWDwGFumsIZyE3AJgL/Fhg\n" +
                "kkt73zkyWY6reZVqlwIDAQAB","RSA2");
        AlipaySecurityRiskRainscoreQueryRequest request = new AlipaySecurityRiskRainscoreQueryRequest();
        request.setBizContent("{" +
                "\"account_type\":\"MOBILE_NO\"," +
                "\"account\":\"17132147425\"," +
                "\"version\":\"2.0\"" +
                "  }");
        AlipaySecurityRiskRainscoreQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

}
