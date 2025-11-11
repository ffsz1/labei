package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Data;

/**
 * @author chris
 * @date 2019-07-14
 */
@Data
public class CheckRet {

    private Integer code;

    private String msg;

    private TextCheckResult textCheckResult;
}
