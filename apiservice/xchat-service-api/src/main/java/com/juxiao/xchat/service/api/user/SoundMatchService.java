package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.user.dto.UserSoundDTO;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import com.juxiao.xchat.service.api.user.vo.SoundMatchConfBO;

import java.util.List;

/**
 * @Description: 声音匹配
 * @Author: alwyn
 * @Date: 2018/11/26 15:39
 */
public interface SoundMatchService {
    /**
     * 获取魅力变化的用户列表
     * @return
     */
    List<UserSimpleVO> charmUser(IndexParam indexParam);

    /**
     * 获取魅力变化的用户列表V2
     * @return
     */
    List<UserSimpleVO> charmUserV2(Integer gender);

    /**
     * 获取随机匹配的用户
     *
     * @param uid
     * @return
     */
    List<UserSoundDTO> randomUser(Long uid, Integer gender, Integer minAge, Integer maxAge) throws WebServiceException;

    /**
     * 喜欢某个用户
     *
     * @param uid
     * @param likeUid
     * @return
     */
    boolean likeUser(Long uid, Long likeUid) throws WebServiceException;

    /**
     * 获取打招呼的礼物列表
     *
     * @param uid
     * @return
     */
    List<GiftDTO> listGift(Long uid);

    /**
     * 获取用户声音匹配的配置
     *
     * @param uid
     * @return
     */
    SoundMatchConfBO getConfig(Long uid) throws WebServiceException;

    /**
     * 设置用户配置
     *
     * @param confBO
     * @return
     */
    boolean setConfig(SoundMatchConfBO confBO);

    /**
     *
     *
     * @param channelEnum
     * @param uid
     * @param gender
     * @param minAge
     * @param maxAge
     * @return
     * @throws WebServiceException
     */
    List<UserSoundDTO> checkAuditRandomUser(ChannelEnum channelEnum, Long uid, Integer gender, Integer minAge, Integer maxAge)throws WebServiceException;
}
