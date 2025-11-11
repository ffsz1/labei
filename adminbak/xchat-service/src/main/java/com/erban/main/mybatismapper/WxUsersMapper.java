package com.erban.main.mybatismapper;

import com.erban.main.model.WxUsers;
import com.erban.main.model.WxUsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxUsersMapper {
    int countByExample(WxUsersExample example);

    int deleteByExample(WxUsersExample example);

    int deleteByPrimaryKey(Integer wxUid);

    int insert(WxUsers record);

    int insertSelective(WxUsers record);

    List<WxUsers> selectByExample(WxUsersExample example);

    WxUsers selectByPrimaryKey(Integer wxUid);

    int updateByExampleSelective(@Param("record") WxUsers record, @Param("example") WxUsersExample example);

    int updateByExample(@Param("record") WxUsers record, @Param("example") WxUsersExample example);

    int updateByPrimaryKeySelective(WxUsers record);

    int updateByPrimaryKey(WxUsers record);
}
