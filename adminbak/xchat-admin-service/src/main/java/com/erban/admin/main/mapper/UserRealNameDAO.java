package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.UserRealInfoDTO;
import com.erban.admin.main.mapper.query.UserRealNameQuery;

import java.util.List;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/30 13:27
 */
public interface UserRealNameDAO {

    /**
     * 查询身份认证信息
     *
     * @param query
     * @return
     */
    List<UserRealInfoDTO> listRealInfo(UserRealNameQuery query);

    /**
     * 查询用户审核信息
     *
     * @param uid
     * @return
     */
    UserRealInfoDTO getByUid(Long uid);

}
