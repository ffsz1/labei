package com.juxiao.xchat.service.api.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PkVoteCancelUserVO {

    private Long uid;

    private String nick;

    private String avatar;

    public PkVoteCancelUserVO(Long uid, UsersDTO usersDto) {
        if (usersDto == null) {
            return;
        }
        this.uid = uid;
        this.nick = usersDto.getNick();
        this.avatar = usersDto.getAvatar();
    }
}
