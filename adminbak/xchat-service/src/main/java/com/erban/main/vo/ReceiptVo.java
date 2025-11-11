package com.erban.main.vo;

import java.util.Map;

/**
 * Created by 北岭山下 on 2017/7/20.
 */
public class ReceiptVo {

        private int status;
        private Map receipt;

        public int getStatus ( ) {
                return status;
        }

        public void setStatus ( int status ) {
                this.status = status;
        }

        public Map getReceipt ( ) {
                return receipt;
        }

        public void setReceipt ( Map receipt ) {
                this.receipt = receipt;
        }
}
