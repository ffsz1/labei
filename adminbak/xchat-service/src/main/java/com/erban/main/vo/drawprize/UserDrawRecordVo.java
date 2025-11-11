package com.erban.main.vo.drawprize;

import com.erban.main.vo.UserVo;


/**
 * Created by liuguofu on 2017/12/15.
 */
public class UserDrawRecordVo {

    private UserVo userVo;

    private Byte drawStatus;//中奖纪录状态，1创建，2未中奖，3已中奖

    private Byte type;

    private String srcObjName;

    private String drawPrizeName;

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public Byte getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(Byte drawStatus) {
        this.drawStatus = drawStatus;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getSrcObjName() {
        return srcObjName;
    }

    public void setSrcObjName(String srcObjName) {
        this.srcObjName = srcObjName;
    }

    public String getDrawPrizeName() {
        return drawPrizeName;
    }

    public void setDrawPrizeName(String drawPrizeName) {
        this.drawPrizeName = drawPrizeName;
    }
}
