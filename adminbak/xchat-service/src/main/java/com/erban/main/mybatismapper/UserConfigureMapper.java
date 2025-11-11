package com.erban.main.mybatismapper;

import com.erban.main.model.UserConfigure;
import com.erban.main.model.UserConfigureExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserConfigureMapper {
    int countByExample(UserConfigureExample example);

    int deleteByExample(UserConfigureExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UserConfigure record);

    int insertSelective(UserConfigure record);

    List<UserConfigure> selectByExample(UserConfigureExample example);

    UserConfigure selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UserConfigure record, @Param("example") UserConfigureExample example);

    int updateByExample(@Param("record") UserConfigure record, @Param("example") UserConfigureExample example);

    int updateByPrimaryKeySelective(UserConfigure record);

    int updateByPrimaryKey(UserConfigure record);

    List<Long> selectByUid();
}
