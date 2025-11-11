package com.juxiao.xchat.dao.bill.domain;

import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Deprecated
public class BillRecordDO {

    private String billId;

    private Long uid;

    private Long targetUid;

    private Long roomUid;

    private Byte billStatus;

    private String objId;

    private Byte objType;

    private Integer giftId;

    private Integer giftNum;

    private Double diamondNum;

    private Long goldNum;

    private Long money;

    private Date createTime;

    private Date updateTime;

    public void setObjType(Byte objType) {
        this.objType = objType;
    }

    public void setObjType(BillRecordType type) {
        if (type != null) {
            this.objType = type.getValue();
        }
    }
}