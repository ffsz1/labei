package com.erban.main.param;

/**
 * Created by 北岭山下 on 2017/7/12.
 */

/**
 * IOS苹果用户支付订单状态
 */

public class ReturnStatusParam {

        private String  chargeRecordID;

        public ReturnStatusParam(){

        }

        public ReturnStatusParam ( String chargeRecordID ) {
                this.chargeRecordID = chargeRecordID;
        }

        public String getChargeRecordID ( ) {
                return chargeRecordID;
        }

        public void setChargeRecordID ( String chargeRecordID ) {
                this.chargeRecordID = chargeRecordID;
        }
}
