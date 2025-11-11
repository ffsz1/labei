package com.erban.main.model;

/**
 * Created by 北岭山下 on 2017/8/4.
 */
public class AuctSum {

        private String auctId;
        private Long sumTotal;


        public String getAuctId ( ) {
                return auctId;
        }

        public void setAuctId ( String auctId ) {
                this.auctId = auctId;
        }

        public Long getSumTotal ( ) {
                return sumTotal;
        }

        public void setSumTotal ( Long sumTotal ) {
                this.sumTotal = sumTotal;
        }
}
