package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:07
 */
public interface FamilyOperateRecordDAO {

    /**
     * 保存操作记录
     * @param familyOperateRecordDO
     * @return
     */
    int save(FamilyOperateRecordDO familyOperateRecordDO);

}
