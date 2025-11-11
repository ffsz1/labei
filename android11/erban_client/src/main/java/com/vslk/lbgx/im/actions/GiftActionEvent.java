package com.vslk.lbgx.im.actions;

import java.io.Serializable;

/**
 * Created by chenran on 2017/11/14.
 */

public class GiftActionEvent implements Serializable {
    public GiftActionEvent() {

    }

    public void onGiftActionEvent(long uid) {

    }

//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        // 序列化过程：必须按成员变量声明的顺序进行封装
//    }
//
//    // 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
//    // 读取Parcel里面数据时必须按照成员变量声明的顺序，Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用
//    public static final Parcelable.Creator<GiftActionEvent> CREATOR = new Creator<GiftActionEvent>() {
//
//        @Override
//        public GiftActionEvent createFromParcel(Parcel source) {
//            return new GiftActionEvent();
//        }
//
//        @Override
//        public GiftActionEvent[] newArray(int size) {
//            return new GiftActionEvent[size];
//        }
//    };
}
