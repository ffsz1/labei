package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置
 *
 * @class: SysConfDO.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Getter
@Setter
public class SysConfDO {
    private String configId;
    private String configName;
    private String configValue;
    private String nameSpace;
    private Byte configStatus;
}