package com.juxiao.xchat.manager.external.netease.ret;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 网易接口返回结果
 *
 * @class: NetEaseRet.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetEaseRet {
    private int code;
    private String desc;

}
