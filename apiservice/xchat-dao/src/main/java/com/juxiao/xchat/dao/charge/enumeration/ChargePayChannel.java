package com.juxiao.xchat.dao.charge.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * @class: ChargePayChannel.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public enum ChargePayChannel {

    /**
     * 支付宝支付
     */
    alipay {
        @Override
        public Map<String, Object> extra(String successUrl) {
            Map<String, Object> extra = new HashMap<>();
            // 可选，开放平台返回的包含账户信息的 token（授权令牌，商户在一定时间内对支付宝某些服务的访问权限）。通过授权登录后获取的
            // alipay_open_id ，作为该参数的 value ，登录授权账户即会为支付账户，32 位字符串。
            // extra.put("extern_token", "TOKEN");
            // 可选，是否发起实名校验，T 代表发起实名校验；F 代表不发起实名校验。
            extra.put("rn_check", "F");
            return extra;
        }
    },
    /**
     * 微信支付
     */
    wx {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    },
    /**
     * 微信公众号支付
     */
    ios_pay {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    },
    /**
     * 微信公众号
     */
    wx_pub {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    },
    /**
     * 微信H5支付
     */
    wx_wap {
        @Override
        public Map<String, Object> extra(String successUrl) {
            Map<String, Object> extra = new HashMap<>();
            extra.put("result_url", successUrl);
            return extra;
        }
    },
    wx_app {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    },
    /**
     * 支付宝H5支付
     */
    alipay_wap {
        @Override
        public Map<String, Object> extra(String successUrl) {
            Map<String, Object> extra = new HashMap<>();
            extra.put("success_url", successUrl);
            return extra;
        }
    },
    /**
     * 钻石兑换
     */
    exchange {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    },
    /**
     * 充值打公账
     */
    companyAccount {
        @Override
        public Map<String, Object> extra(String successUrl) {
            return null;
        }
    };

    public abstract Map<String, Object> extra(String successUrl);
}
