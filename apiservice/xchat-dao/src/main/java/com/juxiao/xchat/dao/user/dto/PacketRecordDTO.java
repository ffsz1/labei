package com.juxiao.xchat.dao.user.dto;

import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户红包记录
 *
 * @class: PacketRecordDTO.java
 * @author: chenjunsheng
 * @date 2018/6/4
 */
@Getter
@Setter
public class PacketRecordDTO {
    /**
     * 账号
     */
    private long uid;
    /**
     * 时间
     */
    private Long date;
    /**
     * 具体时间
     */
    private Date recordTime;
    /**
     * 红包类型
     */
    private String typeStr;
    /**
     * 红包数量
     */
    private Double packetNum;


    public Long getDate() {
        if (date != null) {
            return date;
        }

        if (recordTime == null) {
            this.date = 0L;
        } else {
            Date date = DateTimeUtils.setTime(recordTime, 0, 0, 0);
            this.date = date.getTime();
        }
        return this.date;
    }

    public Long getRecordTime() {
        return recordTime == null ? 0 : recordTime.getTime();
    }

    public void setType(Integer type) {
        UserPacketRecordTypeEnum recordType = UserPacketRecordTypeEnum.typeOf(type);
        if (recordType != null) {
            this.typeStr = recordType.getDesc();
        }
    }
}
