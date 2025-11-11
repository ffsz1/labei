package com.juxiao.xchat.manager.common.item;

import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;

import java.util.List;

/**
 * 座驾管理
 *
 * @class: GiftCarManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface GiftCarManager {
    /**
     * 用户获得座驾
     *
     * @param uid
     * @param carId
     * @param carDate
     */
    void saveUserCar(Long uid, Integer carId, Integer carDate, Integer type, String message);

    /**
     * 用户获得座驾 发送自己消息
     * @param fromUid
     * @param uid
     * @param carId
     * @param carDate
     * @param type
     * @param message
     */
    void saveUserCarWithSelfMsg(Long fromUid,Long uid, Integer carId, Integer carDate, Integer type, String message);
    /**
     * 根据座驾ID获取座驾
     *
     * @param carId
     * @return
     */
    GiftCarDTO getGiftCar(Integer carId);

    /**
     * 获取一台用户拥有的座驾
     *
     * @param uid
     * @return
     */
    GiftCarDTO getUserGiftCar(Long uid);

    /**
     * 获取用户某个座驾
     *
     * @param uid
     * @param carId
     * @return
     */
    GiftCarPurseRecordDTO getUserCarPurseRecord(Long uid, Long carId);

    /**
     * 获取用户座驾（ID）
     *
     * @param uid
     * @return
     */
    List<String> listUserCarids(Long uid);

    /**
     * 加载所有的座驾ID
     *
     * @return
     */
    List<String> listAllGiftCarids();

    /**
     * 查询商城座驾
     * @return
     */
    List<String> listByMall();
}
