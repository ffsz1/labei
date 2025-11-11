package com.juxiao.xchat.dao.user.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 获取红包账单记录
 *
 * @class: PacketRecordQuery.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Getter
@Setter
public class PacketRecordQuery {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 查询日期
     */
    private Date date;
    /**
     * 查询页数
     */
    private Integer startRecord;
    /**
     * 每页查询数量
     */
    private Integer pageSize;

    public PacketRecordQuery(Long uid, Date date, Integer page, Integer pageSize) {
        this.uid = uid;
        this.date = date;
        this.pageSize = pageSize == null || pageSize <= 0 ? 50 : pageSize;
        this.startRecord = ((page == null || page <= 0 ? 1 : page) - 1) * pageSize;
    }
}
