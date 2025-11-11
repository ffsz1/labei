package com.erban.main.mybatismapper;

import com.erban.main.model.UserRealNamePass;
import com.erban.main.model.UserRealNamePassExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRealNamePassMapper {
    int countByExample(UserRealNamePassExample example);

    int deleteByExample(UserRealNamePassExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserRealNamePass record);

    int insertSelective(UserRealNamePass record);

    List<UserRealNamePass> selectByExample(UserRealNamePassExample example);

    UserRealNamePass selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserRealNamePass record, @Param("example") UserRealNamePassExample example);

    int updateByExample(@Param("record") UserRealNamePass record, @Param("example") UserRealNamePassExample example);

    int updateByPrimaryKeySelective(UserRealNamePass record);

    int updateByPrimaryKey(UserRealNamePass record);
}
