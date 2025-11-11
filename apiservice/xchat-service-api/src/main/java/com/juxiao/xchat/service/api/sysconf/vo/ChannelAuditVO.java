package com.juxiao.xchat.service.api.sysconf.vo;

import lombok.Data;

/**
 * 渠道审核VO
 *
 * @Auther: alwyn
 * @Description: 渠道审核信息
 * @Date: 2018/10/24 10:10
 */
@Data
public class ChannelAuditVO {
    /**
     * 是否开启审核模式
     */
    boolean audit;
}
