package com.erban.main.vo;

/**
 * Created by 北岭山下 on 2017/7/24.
 */
/*
                         微信订单查询结果
 */
public class OrderQueryResult {

        private String appid;                           //应用ID
        private  String  mch_id;                        //商店ID
        private String nonce_str;                       //随机字符串
        private String device_info;
        private String sign;                                    //签名
        //============================//
        private String result_code;                     //返回结果
        private String openid;                                  //openid
        private String is_subscribe;                    //是否关注公众号
        private String trade_type;                              //交易类型
        private String bank_type;                               //银行类型
        private String total_fee;                              //交易金额，订单总金额，单位为分
        private String attach;                                         //附加内容
        private String out_trade_no;                            //商品订单号
        private String time_end;                                //交易结束时间
        private String trade_state;                             //订单状态

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

        public String getNonce_str ( ) {
                return nonce_str;
        }

        public void setNonce_str ( String nonce_str ) {
                this.nonce_str = nonce_str;
        }

        public String getDevice_info ( ) {
                return device_info;
        }

        public void setDevice_info ( String device_info ) {
                this.device_info = device_info;
        }

        public String getSign ( ) {
                return sign;
        }

        public void setSign ( String sign ) {
                this.sign = sign;
        }

        public String getResult_code ( ) {
                return result_code;
        }

        public void setResult_code ( String result_code ) {
                this.result_code = result_code;
        }

        public String getOpenid ( ) {
                return openid;
        }

        public void setOpenid ( String openid ) {
                this.openid = openid;
        }

        public String getIs_subscribe ( ) {
                return is_subscribe;
        }

        public void setIs_subscribe ( String is_subscribe ) {
                this.is_subscribe = is_subscribe;
        }

        public String getTrade_type ( ) {
                return trade_type;
        }

        public void setTrade_type ( String trade_type ) {
                this.trade_type = trade_type;
        }

        public String getBank_type ( ) {
                return bank_type;
        }

        public void setBank_type ( String bank_type ) {
                this.bank_type = bank_type;
        }

        public String getTotal_fee ( ) {
                return total_fee;
        }

        public void setTotal_fee ( String total_fee ) {
                this.total_fee = total_fee;
        }

        public String getAttach ( ) {
                return attach;
        }

        public void setAttach ( String attach ) {
                this.attach = attach;
        }

        public String getOut_trade_no ( ) {
                return out_trade_no;
        }

        public void setOut_trade_no ( String out_trade_no ) {
                this.out_trade_no = out_trade_no;
        }

        public String getTime_end ( ) {
                return time_end;
        }

        public void setTime_end ( String time_end ) {
                this.time_end = time_end;
        }

        public String getTrade_state ( ) {
                return trade_state;
        }

        public void setTrade_state ( String trade_state ) {
                this.trade_state = trade_state;
        }
}
