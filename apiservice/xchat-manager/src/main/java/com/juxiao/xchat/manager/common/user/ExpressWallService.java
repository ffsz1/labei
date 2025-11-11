package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.ExpressWallDTO;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 表白墙
 * @Date: 2018/10/8 15:46
 */
public interface ExpressWallService {

    /**
     * 保存表白记录
     * @param message 礼物消息
     */
    void save(GiftMessage message);

    /**
     * 查询- 置顶的记录
     * @return 缓存中没有的话,返回 null
     */
    ExpressWallDTO getTop();

    /**
     * 查询用户表白列表
     */
    List<ExpressWallDTO> findByPage(Integer pageNum, Integer pageSize);
}
