package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 家族操作记录
 * @date 2018/11/26
 * @time 09:55
 */
@Data
public class FamilyOperateRecordDO {


    private Long id;

    private Long itemId;

    private Long uid;

    private Long operateUid;

    private String operate;

    private String txt;

    private String ext;

    private Date createTime;

    public FamilyOperateRecordDO(){}

    public FamilyOperateRecordDO(Long itemId,Long uid,Long operateUid,String operate,String txt,String ext,Date createTime){
        this.itemId = itemId;
        this.uid = uid;
        this.operateUid = operateUid;
        this.operate = operate;
        this.txt = txt;
        this.ext = ext;
        this.createTime = createTime;
    }
}
