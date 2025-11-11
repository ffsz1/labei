package com.erban.main.mybatismapper;

import com.erban.main.model.Users;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9.
 */
public interface UsersMapperExpend {
    List<Users> selectByPage(Map<String, Object> param);

    long selectCount(Map<String, Object> param);

}
