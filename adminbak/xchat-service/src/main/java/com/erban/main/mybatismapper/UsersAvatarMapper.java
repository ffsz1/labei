package com.erban.main.mybatismapper;

import com.erban.main.model.UsersAvatar;
import com.erban.main.model.UsersAvatarExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersAvatarMapper {
    int countByExample(UsersAvatarExample example);

    int deleteByExample(UsersAvatarExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UsersAvatar record);

    int insertSelective(UsersAvatar record);

    List<UsersAvatar> selectByExample(UsersAvatarExample example);

    UsersAvatar selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UsersAvatar record, @Param("example") UsersAvatarExample example);

    int updateByExample(@Param("record") UsersAvatar record, @Param("example") UsersAvatarExample example);

    int updateByPrimaryKeySelective(UsersAvatar record);

    int updateByPrimaryKey(UsersAvatar record);
}
