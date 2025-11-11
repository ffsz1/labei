package com.juxiao.xchat.manager.external.netease.params;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class TextCheckParams {

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 数据唯一标识，能够根据该值定位到该条数据，如对数据结果有异议，可以发送该值给客户经理查询
     */
    private String dataId;

    /**
     * 用户发表内容，建议对内容中JSON、表情符、HTML标签、UBB标签等做过滤，只传递纯文本，以减少误判概率
     */
    private String content;

    /**
     * 用户IP地址
     */
    private String ip;

    /**
     * 用户唯一标识，如果无需登录则为空
     */
    private String account;

    /**
     * 用户设备类型，1：web， 2：wap， 3：android， 4：iphone， 5：iPad， 6：pc， 7：wp
     */
    private Integer deviceType;

    /**
     * 用户设备 id
     */
    private String deviceId;


}
