package com.vslk.lbgx.event;

import org.greenrobot.eventbus.EventBus;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class ToHim extends BaseEven {
    private long uid;

    public static void postToHim(long uid, String className) {
        ToHim toHim = new ToHim();
        toHim.setUid(uid);
        toHim.setClassName(className);
        EventBus.getDefault().post(toHim);
    }
}



