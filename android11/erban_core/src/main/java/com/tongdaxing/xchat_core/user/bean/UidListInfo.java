package com.tongdaxing.xchat_core.user.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class UidListInfo implements Serializable {
    public long uid;

    public boolean valid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
