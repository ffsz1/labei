package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.AppActivityWinDTO;

import java.util.List;

/**
 * 活动查询服务类
 *
 * @class: ActivityService.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface ActivityService {

    /**
     * @param alertWinLoc 弹窗位置：1.首页，2.直播间
     * @param uid         用户ID
     * @return
     */
    List<AppActivityWinDTO> listWinActivity(Integer alertWinLoc, Long uid) throws WebServiceException;

    /**
     * @param uid         用户ID
     * @return
     */
    List<AppActivityWinDTO> listAllActivity(Long uid) throws WebServiceException;
}
