package com.juxiao.xchat.service.api.family;

import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;

/**
 * @author chris
 * @Title:
 * @date 2018/9/25
 * @time 21:12
 */
public interface FamilyOperateRecordService {

    /**
     * 保存操作记录
     * @param familyOperateRecordDO
     * @return
     */
    int save(FamilyOperateRecordDO familyOperateRecordDO);
}
