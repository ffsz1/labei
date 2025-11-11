package com.erban.main.param.neteasepush;

import java.util.List;

/**
 * Created by liuguofu on 2017/7/13.
 */
public class NeteasePushBatchParam  extends NeteasePushBase{
    private String fromAccid;//		是	发送者accid，用户帐号，最大32字符，APP内唯一
    private List<String> toAccids;//		是	["aaa","bbb"]（JSONArray对应的accid，如果解析出错，会报414错误），最大限500人

    public String getFromAccid() {
        return fromAccid;
    }

    public void setFromAccid(String fromAccid) {
        this.fromAccid = fromAccid;
    }

    public List<String> getToAccids() {
        return toAccids;
    }

    public void setToAccids(List<String> toAccids) {
        this.toAccids = toAccids;
    }
}
