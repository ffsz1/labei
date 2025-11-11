package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;
import com.juxiao.xchat.dao.user.dto.UserPacketRegTeamDTO;
import com.juxiao.xchat.service.api.user.vo.StatPacketActivityParentVO;
import com.juxiao.xchat.service.api.user.vo.StatPacketActivityVO;
import com.juxiao.xchat.service.api.user.vo.StatPacketBounsDetailParentVO;
import com.juxiao.xchat.service.api.user.vo.StatPacketInviteDetailParentVO;

import java.util.List;

public interface StatPacketActivityService {

    /**
     * 我的红包页面(邀请人数，分成奖励，红包金额)
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    StatPacketActivityVO getUserStatPacketDetail(Long uid) throws WebServiceException;

    /**
     * 红包排行榜页面
     *
     * @param uid
     * @return
     */
    StatPacketActivityParentVO listPacketActivityRank(Long uid) throws WebServiceException;

    /**
     * 我的邀请详情页面接口
     *
     * @param uid
     * @return
     */
    StatPacketInviteDetailParentVO getInviteDetail(Long uid) throws WebServiceException;

    /**
     * @param uidList
     * @return
     */
    List<StatPacketActivityDTO> listSomeInviteDetail(String uidList) throws WebServiceException;

    /**
     * 我的团队
     *
     * @param uid
     * @return
     */
    List<UserPacketRegTeamDTO> listUserTeam(Long uid) throws WebServiceException;

    /**
     * @param uid
     * @return
     * @throws WebServiceException
     */
    StatPacketBounsDetailParentVO getBounsDetail(Long uid) throws WebServiceException;


}
