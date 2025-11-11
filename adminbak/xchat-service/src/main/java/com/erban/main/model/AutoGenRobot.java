package com.erban.main.model;

/**
 * 自动生成机器人
 */
public class AutoGenRobot {
    private Integer id;
    private String nick;
    private String avatar;
    private Byte gender;
    private Byte isUsed;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Byte getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Byte isUsed) {
        this.isUsed = isUsed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AutoGenRobot{" +
                "id=" + id +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", isUsed=" + isUsed +
                ", password='" + password + '\'' +
                '}';
    }
}
