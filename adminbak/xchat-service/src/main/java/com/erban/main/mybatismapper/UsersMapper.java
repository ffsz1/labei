package com.erban.main.mybatismapper;

import com.erban.main.dto.UsersDTO;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import java.util.List;

import com.erban.main.vo.NewUserParam;
import com.erban.main.vo.SimpleUserVo;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int countByExample(UsersExample example);

    int deleteByExample(UsersExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(Users record);

    int insertSelective(Users record);

    List<Users> selectByExample(UsersExample example);

    Users selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByExample(@Param("record") Users record, @Param("example") UsersExample example);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    List<SimpleUserVo> selectNewUser(NewUserParam param);


    UsersDTO getUser(@Param("uid") Long uid);

    int updateByDefaultTag(@Param("uid")Long uid);
}
