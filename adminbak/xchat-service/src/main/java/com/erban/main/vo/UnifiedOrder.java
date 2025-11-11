package com.erban.main.vo;

/**
 * Created by 北岭山下 on 2017/7/24.
 */
/*
                微信统一下单订单
                <xml>
           <appid>wx2421b1c4370ec43b</appid>
           <attach>支付测试</attach>
           <body>JSAPI支付测试</body>
           <mch_id>10000100</mch_id>
           <detail><![CDATA[{ "goods_detail":[ { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001", "goods_name":"iPhone6s 16G", "quantity":1, "price":528800, "goods_category":"123456", "body":"苹果手机" }, { "goods_id":"iphone6s_32G", "wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1, "price":608800, "goods_category":"123789", "body":"苹果手机" } ] }]]></detail>
           <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
           <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
           <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
           <out_trade_no>1415659990</out_trade_no>
           <spbill_create_ip>14.23.150.211</spbill_create_ip>
           <total_fee>1</total_fee>
           <trade_type>JSAPI</trade_type>
           <sign>0CB01533B8C1EF103065174F50BCA001</sign>
        </xml>

 */
public class UnifiedOrder {

        private String appid;
        private String mch_id;
        private String attach;
        private String body;
        private String detail;
        private String nonce_str;
        private String notify_url;
        private String openid;
        private String out_trade_no;
        private String spbill_create_ip;
        private String total_fee;
        private String trade_type;
        private String sign;


        public String getAppid ( ) {
                return appid;
        }

        public void setAppid ( String appid ) {
                this.appid = appid;
        }

        public String getMch_id ( ) {
                return mch_id;
        }

        public void setMch_id ( String mch_id ) {
                this.mch_id = mch_id;
        }

        public String getAttach ( ) {
                return attach;
        }

        public void setAttach ( String attach ) {
                this.attach = attach;
        }

        public String getBody ( ) {
                return body;
        }

        public void setBody ( String body ) {
                this.body = body;
        }

        public String getDetail ( ) {
                return detail;
        }

        public void setDetail ( String detail ) {
                this.detail = detail;
        }

        public String getNonce_str ( ) {
                return nonce_str;
        }

        public void setNonce_str ( String nonce_str ) {
                this.nonce_str = nonce_str;
        }

        public String getNotify_url ( ) {
                return notify_url;
        }

        public void setNotify_url ( String notify_url ) {
                this.notify_url = notify_url;
        }

        public String getOpenid ( ) {
                return openid;
        }

        public void setOpenid ( String openid ) {
                this.openid = openid;
        }

        public String getOut_trade_no ( ) {
                return out_trade_no;
        }

        public void setOut_trade_no ( String out_trade_no ) {
                this.out_trade_no = out_trade_no;
        }

        public String getSpbill_create_ip ( ) {
                return spbill_create_ip;
        }

        public void setSpbill_create_ip ( String spbill_create_ip ) {
                this.spbill_create_ip = spbill_create_ip;
        }

        public String getTotal_fee ( ) {
                return total_fee;
        }

        public void setTotal_fee ( String total_fee ) {
                this.total_fee = total_fee;
        }

        public String getTrade_type ( ) {
                return trade_type;
        }

        public void setTrade_type ( String trade_type ) {
                this.trade_type = trade_type;
        }

        public String getSign ( ) {
                return sign;
        }

        public void setSign ( String sign ) {
                this.sign = sign;
        }
}
