package com.erban.main.mybatismapper;

import com.erban.main.model.UserDraw;
import com.erban.main.model.UserDrawExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDrawMapper {
    int countByExample(UserDrawExample example);

    int deleteByExample(UserDrawExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UserDraw record);

    int insertSelective(UserDraw record);

    List<UserDraw> selectByExample(UserDrawExample example);

    UserDraw selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UserDraw record, @Param("example") UserDrawExample example);

    int updateByExample(@Param("record") UserDraw record, @Param("example") UserDrawExample example);

    int updateByPrimaryKeySelective(UserDraw record);

    int updateByPrimaryKey(UserDraw record);
}
