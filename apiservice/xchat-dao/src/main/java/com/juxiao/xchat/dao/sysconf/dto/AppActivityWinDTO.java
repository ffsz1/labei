package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @class: AppActivityWinDTO.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
public class AppActivityWinDTO {

    private Integer actId;

    private String actName;

    private String actAlertVersion;

    private Integer alertWin;

    private String alertWinPic;

    private Byte alertWinLoc;

    private Byte skipType;

    private String skipUrl;

    public Boolean getAlertWin() {
        return alertWin != null && alertWin.intValue() == 1;
    }


}
