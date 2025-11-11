package com.juxiao.xchat.service.record.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomCtrbTopDTO;
import com.juxiao.xchat.dao.room.dto.StatRoomUserCtrbSumDTO;

import java.util.List;

/**
 * 房间贡献榜服务层
 *
 * @class: StatRoomCtrbSumService.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
public interface StatRoomCtrbSumService {

    /**
     * 查询房间贡献榜，分为财富、魅力分栏，下面有日、周、总榜
     *
     * @param uid      房主ID
     * @param dataType 1：日；2：周；3：总榜
     * @param type     1：财富；2：魅力分栏
     * @return
     */
    List<StatRoomUserCtrbSumDTO> listRoomContribution(Long uid, Integer dataType, Integer type) throws WebServiceException;


    List<RoomCtrbTopDTO> listRoomCtrbTop(Long roomUid) throws WebServiceException;
}
