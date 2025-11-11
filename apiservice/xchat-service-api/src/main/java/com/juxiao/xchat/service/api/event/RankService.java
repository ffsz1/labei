package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.event.vo.RankVo;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.RankHomeVo;
import com.juxiao.xchat.service.api.event.vo.RankParentVo;

public interface RankService {

    /**
     * 查询缓存中的排行信息
     *
     * @return
     */
    RankHomeVo getRankHomeByCache();

    /**
     * 查询各类礼物排行榜信息，H5页专用
     * @param type 排行榜类型：1巨星榜；2贵族榜；3房间榜
     * @param datetype 榜单统计周期类型：1、日榜；2周榜；3总榜
     * @param pageSize 数据条数
     * @return
     */
    RankParentVo getH5RankList(Integer type, Integer datetype,Integer pageSize);


    RankParentVo getAuditingRankList(ChannelEnum channelEnum, Integer type, Integer datetype);

    RankVo getMeh5Rank(Integer type, Integer datetype, Long uid)throws WebServiceException;
}
