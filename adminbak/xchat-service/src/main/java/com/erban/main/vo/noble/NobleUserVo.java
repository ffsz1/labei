package com.erban.main.vo.noble;

import com.erban.main.model.NobleUsers;

public class NobleUserVo implements Comparable<NobleUserVo>{
    private Long uid;
    private String avatar;
    private NobleUsers nobleUsers;
    private String nick;
    private Byte gender;
    private Long enterTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public NobleUsers getNobleUsers() {
        return nobleUsers;
    }

    public void setNobleUsers(NobleUsers nobleUsers) {
        this.nobleUsers = nobleUsers;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Long enterTime) {
        this.enterTime = enterTime;
    }

    @Override
    public int compareTo(NobleUserVo nobleUserVo){
        Integer nobleId = nobleUserVo.getNobleUsers().getNobleId();
        Integer thisNobleId = this.nobleUsers.getNobleId();

        Long enterTime = nobleUserVo.getEnterTime();
        Long thisEnterTime = this.getEnterTime();

        if(nobleId > thisNobleId){
            return 1;
        }else if(nobleId == thisNobleId){
            if(enterTime < thisEnterTime){
                return 1;
            }else if(enterTime > thisEnterTime){
                return -1;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
    }
}
