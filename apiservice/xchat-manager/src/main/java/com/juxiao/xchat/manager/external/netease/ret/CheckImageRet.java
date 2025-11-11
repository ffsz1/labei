package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class CheckImageRet {

    private Integer code;

    private String msg;

    private ImageCheckRet imageCheckRet;

    /**
     * 分类级别，0：正常，1：不确定，2：确定
     */
    private Integer level;

    private String result;
}
