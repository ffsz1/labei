package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoGenRobot {
    private String nick;
    private String avatar;
    private Byte gender;
    private Byte isUsed;
}