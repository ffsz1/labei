package com.juxiao.xchat.manager.mq.bo;

import com.juxiao.xchat.dao.user.dto.UsersDTO;
import lombok.Data;

@Data
public class UpdateUserInfoMessageBO {
    //
    private UsersDTO usersDTO;

    public UpdateUserInfoMessageBO(UsersDTO usersDTO) {
        this.usersDTO = usersDTO;
    }
}
