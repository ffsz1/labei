package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class ImageCheckRet {

    private String name;

    /**
     * 图片检测状态码，定义为：0：检测成功，610：图片下载失败，620：图片格式错误，630：其它
     */
    private Integer status;

    private String taskId;


}
