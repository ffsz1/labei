package com.erban.main.model;

/**
 * Created by 北岭山下 on 2017/8/8.
 */
public class StatRoomAuctVo {

        private Long uid;
        private Integer  total_auct_money;
        private Integer  total_auct_count;

        public Long getUid ( ) {
                return uid;
        }

        public void setUid ( Long uid ) {
                this.uid = uid;
        }

        public Integer getTotal_auct_money ( ) {
                return total_auct_money;
        }

        public void setTotal_auct_money ( Integer total_auct_money ) {
                this.total_auct_money = total_auct_money;
        }

        public Integer getTotal_auct_count ( ) {
                return total_auct_count;
        }

        public void setTotal_auct_count ( Integer total_auct_count ) {
                this.total_auct_count = total_auct_count;
        }
}
