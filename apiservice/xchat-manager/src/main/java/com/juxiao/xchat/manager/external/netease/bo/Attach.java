package com.juxiao.xchat.manager.external.netease.bo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attach {
    private int first;
    private int second;
    private Object data;


    public static class MsgType {
        /** 发送点对点消息 */
        public static int PointToPoint = 10;
        /** 发送图文消息 */
        public static int PushPicWord = 101;
    }
}
