package com.juxiao.xchat.manager.external.netease.bo;


public class BaseNeteaseMsgBO {
    protected Attach attach;//	String	是	自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符
    protected String pushcontent;//	String	否	iOS推送内容，第三方自己组装的推送内容,不超过150字符
    protected Payload payload;//	String	否	iOS推送对应的payload,必须是JSON,不能超过2k字符
    protected String sound;//	String	否	如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字符，如果不指定，会使用默认声音
    protected int save;//	int	否	1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线
    protected Option option;//	String	否	发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
