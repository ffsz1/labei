package com.erban.main.param.neteasepush;

import java.util.List;

/**
 * Created by liuguofu on 2017/7/13.
 */
public class NeteaseSendMsgBatchParam{
    private String fromAccid;//		是	发送者accid，用户帐号，最大32字符，APP内唯一
    private List<String> toAccids;//		是	["aaa","bbb"]（JSONArray对应的accid，如果解析出错，会报414错误），最大限500人

    private int type;
    private Body body;
    private Option option;
    private String pushcontent;
    private Payload payload;
    private String  ext;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getFromAccid() {
        return fromAccid;
    }


    public Option getOption() {
        return option;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getPushcontent() {
        return pushcontent;
    }

    public void setPushcontent(String pushcontent) {
        this.pushcontent = pushcontent;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
