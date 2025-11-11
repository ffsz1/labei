package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.user.domain.AccompanyTypeDO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.service.api.user.vo.AccompanyManualVO;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 15:29
 */
public interface AccompanyManualService {


    /**
     * 获取陪玩推荐列表
     * @param uid
     * @param os
     * @param appVersion
     * @param app
     * @return
     */
    List<AccompanyManualVO> getList(Long uid, Integer pageNum, Integer pageSize, String os, String appVersion, String app, String type);

    /**
     * 获取陪玩推荐逻辑金额
     * @return
     */
    String getAccompanyMoney();


    /**
     * 获取陪玩分类列表
     * @param uid
     * @param os
     * @param appVersion
     * @param app
     * @return
     */
    List<AccompanyTypeDO> getAccompanyTypeByList(Long uid, String os, String appVersion, String app, String channel);

    /**
     * 审核陪玩
     * @return
     */
    List<RoomVo> getCheckAudition();

    List<BannerDTO> getAccompanyBanner(Long uid, String os, String app);
}
