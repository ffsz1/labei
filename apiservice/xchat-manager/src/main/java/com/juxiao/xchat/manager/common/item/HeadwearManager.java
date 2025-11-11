package com.juxiao.xchat.manager.common.item;

import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;

import java.util.List;

/**
 * @class: HeadwearManager.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface HeadwearManager {

    /**
     * 用户获得头饰
     *
     * @param uid
     * @param headwearId
     */
    void saveUserHeadwear(Long uid, Integer headwearId, Integer headwearDate, Integer type, String message);

    /**
     * 用户获得头饰 limself 2020/10/21
     * @param fromUid 赠送人的uid
     * @param uid  接受的 uid
     * @param headwearId 头饰id
     * @param headwearDate 头饰 日期
     * @param type  类型
     * @param message  信息提示
     */
    void saveUserHeadwearWithSelfMsg(Long fromUid,Long uid, Integer headwearId, Integer headwearDate, Integer type, String message);
    /**
     * 根据头饰ID获取头饰
     *
     * @param headwearId
     * @return
     */
    HeadwearDTO getHeadwear(Integer headwearId);

    /**
     * 获取用户其中头饰
     *
     * @param uid
     * @return
     */
    HeadwearDTO getUserHeadwear(Long uid);

    /**
     * @param uid
     * @param headwearId
     * @return
     */
    HeadwearPurseRecordDTO getHeadwearRecord(Long uid, Integer headwearId);

    /**
     * @param uid
     * @return
     */
    List<String> listUserHeadwearid(Long uid);

    /**
     * @return
     */
    List<String> listAllHeadwearids();

    /**
     * 获取商城头饰
     * @return
     */
    List<String> listByMall();
}
