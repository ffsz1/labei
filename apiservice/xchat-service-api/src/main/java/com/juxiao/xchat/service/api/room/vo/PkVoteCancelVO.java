package com.juxiao.xchat.service.api.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PkVoteCancelVO {

    private PkVoteCancelUserVO opUser;

    private PkVoteCancelUserVO user;

    private PkVoteCancelUserVO pkUser;

    public Long getOpUid() {
        return opUser == null ? null : opUser.getUid();
    }

    public String getOpNick() {
        return opUser == null ? null : opUser.getNick();
    }

    public String getOpAvatar() {
        return opUser == null ? null : opUser.getAvatar();
    }

    public Long getUid() {
        return user == null ? null : user.getUid();
    }

    public String getNick() {
        return user == null ? null : user.getNick();
    }

    public String getAvatar() {
        return user == null ? null : user.getAvatar();
    }

    public Long getPkUid() {
        return pkUser == null ? null : pkUser.getUid();
    }

    public String getPkNick() {
        return pkUser == null ? null : pkUser.getNick();
    }

    public String getPkAvatar() {
        return pkUser == null ? null : pkUser.getAvatar();
    }
}
