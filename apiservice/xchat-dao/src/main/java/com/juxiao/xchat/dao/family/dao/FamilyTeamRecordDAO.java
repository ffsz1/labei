package com.juxiao.xchat.dao.family.dao;

import com.juxiao.xchat.dao.family.domain.FamilyTeamRecordDO;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:08
 */
public interface FamilyTeamRecordDAO {

    /**
     * 批量插入家族流水记录
     * @param familyTeamRecord
     * @return
     */
    int batchInsert(List<FamilyTeamRecordDO> familyTeamRecord);

}
