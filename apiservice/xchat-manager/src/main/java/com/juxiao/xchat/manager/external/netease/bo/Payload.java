package com.juxiao.xchat.manager.external.netease.bo;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payload {
    private int skiptype;
    private Object data;

    public static class SkipType {
        /**
         * 跳app页面
         */
        public static final int APPPAGE = 1;
        /**
         * 跳聊天室
         */
        public static final int ROOM = 2;
        /**
         * 跳h5页面
         */
        public static final int H5 = 3;
    }
}
