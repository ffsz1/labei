package com.erban.main.service;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.erban.main.config.PingxxConfig;
import com.erban.main.model.SysConf;
import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuguofu on 2017/6/27.
 */
@Service
public class PingxxService {
    private static final Logger logger = LoggerFactory.getLogger(PingxxService.class);
    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */
//    private final static String apiKey = "sk_test_ibbTe5jLGCi5rzfH4OqPW9KC";

    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
//    private final static String appId = PropertyUtil.getProperty("pingxxApiId");

    /**
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
     * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
     * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
     */
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    PingxxConfig pingxxConfig;

    // 你生成的私钥路径
    private final static String privateKeyFilePath = "res/your_rsa_private_key_pkcs8.pem";

    @PostConstruct
    public void init()
    {
        Pingpp.apiKey = pingxxConfig.getApiKey();
        Pingpp.privateKeyPath=pingxxConfig.getPrivateKeyPath();
    }

//    static {
        // 设置 API Key
//        Pingpp.apiKey = PropertyUtil.getProperty("pingxxApiKey");

        // 设置私钥路径，用于请求签名
//        Pingpp.privateKeyPath = privateKeyFilePath;

        /**
         * 或者直接设置私钥内容
         Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
         "... 私钥内容字符串 ...\n" +
         "-----END RSA PRIVATE KEY-----\n";
         */
//        Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
//                "MIICXQIBAAKBgQC4FxEYbLidyIhfxvlcDsGy6rOkWH6kN/eFIPyO1vm6/qybYuGp\n" +
//                "97xVgb6VYg7oYXa1jeGgfDXzgNMZik4bJmGJAjSGK7yr253WHsfynvAixnZNpsRX\n" +
//                "vMrfPS+xr9RpHiYMQmTegJAOg7IfKcvzNW7CkTYwwOUBmQ5GGeX6uGi1IQIDAQAB\n" +
//                "AoGAfAPeGmwzuue7J3qtxhyS1OtT9eU/+3VQpDjiq3+bYSPsOAnXKF+iWqA7OfuD\n" +
//                "O31dMVXRqAHFfrNMgyovEA89KKAIZ89nyXyBxpL9BrQ+dFeb+3vBqBl5pX7gv7tw\n" +
//                "tRoCKJdfth3sFWl+UKNKmQ1Y0+EuEnol2vX1IqyWSk9hjyECQQDrS4lrpIcgB59m\n" +
//                "JehR7skqw0KzWce0/kkLCmgP5WGtJRH1lqqhDyyUHQCUKt4JaTGXW5z8UAt/uISW\n" +
//                "rQznjgYFAkEAyEoKxaPSdpEU6XsrYRKaS4Fj8kjl2h7oLrMxx9MCC3RlwkqL+e50\n" +
//                "CQEbvtxCY2itY1+nHymsMYaBXEw+fDuhbQJBALFE7JiDkSYpeAb+YkRwMXEdQQBc\n" +
//                "kKAkwCUoRvRK7ccD2b78Lzs+cmh4XTpD/Zp7fVg6NlDFgZRNEN9wKvu9PX0CQQCu\n" +
//                "OToFkeuRl8JJFws09amh5Hu8Vg0bH6d4eAflendc33Nk/tOkJPhqnLw12gMl1fh9\n" +
//                "aMdHJEjVC9ubHfd8uaJBAkAeYGrW4bPlf7nrC6X1XXibm59qOmKUm+Jw3Q2ea2h3\n" +
//                "wsyGRRR3ST8P+GcxPvzg7SbOAbmQ1Epa8Z5sWeGuuWM7\n" +
//                "-----END RSA PRIVATE KEY-----";
//    }


    public Charge charge(Map<String, Object> chargeMap) throws Exception {
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", pingxxConfig.getAppId());
        chargeMap.put("app", app);
        Charge charge = Charge.create(chargeMap);
        return charge;

    }

    /**
     * 查询charge对象
     * @param id
     * @return
     * @throws Exception
     */
    public Charge retrieve(String id) throws Exception{
        Charge charge = null;
        Map<String, Object> params = Maps.newHashMap();
        charge = Charge.retrieve(id, params);
        return charge;
    }

    /**
     * 企业转账(用于后台提现)
     * @param transferMap
     * @return
     * @throws Exception
     */
    public Transfer transfer(Map<String,Object> transferMap) throws Exception {
        Map<String, String> app = new HashMap<>();
        try {
            SysConf sysConf = sysConfService.getSysConfById("alipay");
            if (sysConf != null) {
                if ("1".equals(sysConf.getConfigValue())) {
                    app.put("id", pingxxConfig.getAppId());
                } else if("2".equals(sysConf.getConfigValue())){
                    app.put("id", pingxxConfig.getApiIdSecond());
                }else{
                    app.put("id", pingxxConfig.getApiIdThree());
                }
            } else {
                app.put("id", pingxxConfig.getAppId());
            }
        } catch (Exception e) {
            app.put("id", pingxxConfig.getAppId());
            logger.error("[ 获取转账账号失败 ]", e);
        }
        transferMap.put("app", app);
        logger.info("[ ping++transferMap ] > {}", JSON.toJSONString(transferMap));
        Transfer transfer = Transfer.create(transferMap);
        return transfer;
    }

    /**
     * 查询转账
     * @param id
     * @return
     * @throws Exception
     */
    public Transfer transferRetrieve(String id) throws Exception {
        Transfer transfer = Transfer.retrieve(id);
        return transfer;
    }
}
