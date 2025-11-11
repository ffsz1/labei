package com.juxiao.xchat.service.api.family.impl;

import com.juxiao.xchat.dao.family.dao.FamilyOperateRecordDAO;
import com.juxiao.xchat.dao.family.domain.FamilyOperateRecordDO;
import com.juxiao.xchat.service.api.family.FamilyOperateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title:
 * @date 2018/9/25
 * @time 21:13
 */
@Service
public class FamilyOperateRecordServiceImpl implements FamilyOperateRecordService {
    private final FamilyOperateRecordDAO familyOperateRecordDAO;

    @Autowired
    public FamilyOperateRecordServiceImpl(FamilyOperateRecordDAO familyOperateRecordDAO) {
        this.familyOperateRecordDAO = familyOperateRecordDAO;
    }

    /**
     * 保存操作记录
     *
     * @param familyOperateRecordDO
     * @return
     */
    @Override
    public int save(FamilyOperateRecordDO familyOperateRecordDO) {
        return familyOperateRecordDAO.save(familyOperateRecordDO);
    }
}
