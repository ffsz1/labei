package com.juxiao.xchat.manager.external.netease.bo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NeteaseBatchMsgBO {
    // 是	发送者accid，用户帐号，最大32字符，APP内唯一
    private String fromAccid;
    // 是	["aaa","bbb"]（JSONArray对应的accid，如果解析出错，会报414错误），最大限500人
    private List<String> toAccids;
    /**
     *
     0 表示文本消息,
     1 表示图片，
     2 表示语音，
     3 表示视频，
     4 表示地理位置信息，
     6 表示文件，
     100 自定义消息类型
     */
    private int type;
    private Body body;
    // 发消息时特殊指定的行为选项
    private Option option;
    // ios推送内容，不超过150字符，option选项中允许推送（push=true），此字段可以指定推送内容
    private String pushcontent;
    private Payload payload;
    private String content;
    private String ext;

    public NeteaseBatchMsgBO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
