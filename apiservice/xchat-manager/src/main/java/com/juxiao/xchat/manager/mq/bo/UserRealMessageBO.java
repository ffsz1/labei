package com.juxiao.xchat.manager.mq.bo;

import com.juxiao.xchat.dao.user.dto.UsersDTO;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2019-05-23
 * @time 19:44
 */
@Data
public class UserRealMessageBO {

    private UsersDTO usersDTO;

    public UserRealMessageBO(UsersDTO usersDTO) {
        this.usersDTO = usersDTO;
    }

}
