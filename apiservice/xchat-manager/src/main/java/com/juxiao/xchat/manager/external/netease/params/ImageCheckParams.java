package com.juxiao.xchat.manager.external.netease.params;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class ImageCheckParams {

    /**
     * 业务ID
     */
    private String businessId;


    /**
     * 用户IP地址
     */
    private String ip;

    /**
     * 用户唯一标识，如果无需登录则为空
     */
    private String account;

    private String[] images;
}
