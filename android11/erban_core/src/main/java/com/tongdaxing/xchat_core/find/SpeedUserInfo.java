package com.tongdaxing.xchat_core.find;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/3
 * 描述        获取速配的公聊信息entity
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class SpeedUserInfo {

    private long uid;
    private String nick;
    private String avatar;
    private String content;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SpeedUserInfo{" +
                "uid=" + uid +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
