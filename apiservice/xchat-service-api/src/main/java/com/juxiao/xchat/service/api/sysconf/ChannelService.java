package com.juxiao.xchat.service.api.sysconf;

import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 渠道信息
 * @Date: 2018/10/22 16:39
 */
@Service
public interface ChannelService {

    /**
     * 根据渠道信息名称, 查询渠道配置信息
     * @param channel
     * @return
     */
    ChannelDTO getByName(ChannelEnum channel);

    /**
     * 根据渠道ID 查询房间列表
     * @param id
     * @return
     */
    List<Long> listByChannelId(Integer id);

    /**
     * 检查是否开启审核模式
     * @param channel
     * @param appVersion
     * @param uid
     * @return
     */
    ChannelAuditVO checkAudit(ChannelEnum channel, String appVersion, Long uid);


    /**
     * 根据渠道ID 查询Icon列表
     * @param id
     * @return
     */
    List<Integer> listIconByChannelId(Integer id);

    /**
     * 根据渠道ID 查询Banner列表
     * @param id
     * @return
     */
    List<Integer> listBannerByChannelId(Integer id);
}
