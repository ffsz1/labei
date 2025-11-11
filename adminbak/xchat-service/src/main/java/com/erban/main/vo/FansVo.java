package com.erban.main.vo;

public class FansVo implements Comparable<FansVo> {

    private String nick;

    private Long uid;

    private String avatar;

    private boolean valid;

    public void setNick(String nick) {
        this.nick = nick;
    }


    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getNick() {

        return nick;
    }

    public Long getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public int compareTo(FansVo fansVo) {
        boolean ValidVo = fansVo.isValid();
        boolean thisValid = this.valid;
        if (ValidVo) {
            return 1;
        } else if (thisValid) {
            return 0;
        } else {
            return 0;
        }
    }
}
