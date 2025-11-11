package com.juxiao.xchat.manager.common.draw;

import com.juxiao.xchat.dao.room.dto.RoomConfDTO;

/**
 * @author chris
 */
public interface GiftDrawManager {

    /**
     * 是否使用该捡海螺逻辑
     *
     * @param uid         捡海螺用户ID
     * @param roomConfDto 捡海螺房间配置信息
     * @return boolean
     */
    boolean check(Long uid, RoomConfDTO roomConfDto, int totalDrawNum, boolean isXq, boolean isHd);

    /**
     * 执行捡海螺逻辑
     *
     * @param uid     uid
     * @param roomUid roomUid
     * @return 成功返回礼物ID
     */
    int draw(Long uid, Long roomUid, int totalDrawNum, boolean isXq, boolean isHd);
}
