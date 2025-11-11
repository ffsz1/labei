package com.juxiao.xchat.dao.bill.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 礼物支出记录查询参数
 *
 * @class: BillUserQuery.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
@Getter
@Setter
public class BillUserQuery {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 查询日期
     */
    private Date date;
    /**
     * 开始记录数
     */
    private Integer startRecord;
    /**
     * 每页数量
     */
    private Integer prePage;

    public BillUserQuery() {
    }

    public BillUserQuery(Long uid, Date date, Integer startRecord, Integer prePage) {
        this.uid = uid;
        this.date = date;
        this.startRecord = startRecord;
        this.prePage = prePage;
    }
}
