package com.erban.main.model;

/**
 * Created by 北岭山下 on 2017/8/7.
 */
/*<result column="auct_uid" property="auct_uid" jdbcType="BIGINT" />
<result column="COUNT(auct_uid)" property="totalAuctMoney" jdbcType="INTEGER" />
<result column="SUM(deal_money)" property="totalAuctCount" jdbcType="INTEGER" />*/
public class StatAkiraAuctVo {

        private Long auct_uid;
        private Integer totalAuctMoney;
        private Integer totalAuctCount;


        public Long getAuct_uid ( ) {
                return auct_uid;
        }

        public void setAuct_uid ( Long auct_uid ) {
                this.auct_uid = auct_uid;
        }

        public Integer getTotalAuctMoney ( ) {
                return totalAuctMoney;
        }

        public void setTotalAuctMoney ( Integer totalAuctMoney ) {
                this.totalAuctMoney = totalAuctMoney;
        }

        public Integer getTotalAuctCount ( ) {
                return totalAuctCount;
        }

        public void setTotalAuctCount ( Integer totalAuctCount ) {
                this.totalAuctCount = totalAuctCount;
        }
}
