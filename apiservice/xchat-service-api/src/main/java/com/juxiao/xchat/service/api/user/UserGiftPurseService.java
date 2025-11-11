package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.dto.BillGiftDrawDTO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.service.api.item.mq.GiftDrawMessage;
import com.juxiao.xchat.service.api.item.vo.GiftVO;
import com.juxiao.xchat.service.api.user.vo.DrawConchVO;
import com.juxiao.xchat.service.api.user.vo.DrawGiftVO;

import java.util.List;

public interface UserGiftPurseService {
    /**
     * 捡海螺, 更新返回结果, 添加礼物具体信息
     *
     * @param uid    捡海螺用户
     * @param type   1 1次 2 10次
     * @param roomId 捡海螺房间
     * @return 砸中的礼物信息列表
     * @throws WebServiceException
     */
    DrawConchVO doDraw(Long uid, Integer type, Long roomId) throws WebServiceException;

    DrawConchVO xqDraw(Long uid, Integer type, Long roomId) throws WebServiceException;

    DrawConchVO hdDraw(Long uid, Integer type, Long roomId) throws WebServiceException;

    /**
     * 处理捡海螺信息
     *
     * @param roomMessage
     */
    void handleDrawMessage(GiftDrawMessage roomMessage);
    void handleTryDrawMessage(GiftDrawMessage roomMessage);

    /**
     * 捡海螺, 更新返回结果, 添加礼物具体信息
     *
     * @param uid    捡海螺用户
     * @param type   1 1次 2 10次
     * @param roomId 捡海螺房间
     * @return 砸中的礼物信息列表
     * @throws WebServiceException
     */
    List<DrawGiftVO> dyDraw(Long uid, Integer type, Long roomId) throws WebServiceException;

    /**
     * 处理捡海螺信息
     *
     * @param roomMessage
     */
    void handleDyDrawMessage(GiftDrawMessage roomMessage);

    /**
     * 获取捡海螺排行
     *
     * @param type
     * @return
     */
    List<?> getRank(Integer type, Integer giftType);

    /**
     * 获取用户的抽奖记录
     *
     * @param uid
     * @return
     */
    List<BillGiftDrawDTO> record(Long uid, Integer giftType, Integer pageNum, Integer pageSize);

    /**
     * 获取捡海螺奖池礼物
     *
     * @param uid uid
     * @return GiftVO
     */
    List<GiftVO> getPrizePoolGift(Long uid, Integer giftType);

    public void sendGiftOneRoom(RoomDTO roomDto, UsersDTO sendUser, UsersDTO recvUser, GiftDTO giftDTO, Integer giftNum);
}
