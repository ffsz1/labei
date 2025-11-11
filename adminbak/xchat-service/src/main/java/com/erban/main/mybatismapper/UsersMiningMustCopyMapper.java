package com.erban.main.mybatismapper;

import com.erban.main.model.UsersMiningMust;
import com.erban.main.model.UsersMiningMustExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UsersMiningMustCopyMapper {
    int countByExample(UsersMiningMustExample example);

    int deleteByExample(UsersMiningMustExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UsersMiningMust record);

    int insertSelective(UsersMiningMust record);

    List<UsersMiningMust> selectByExample(UsersMiningMustExample example);

    UsersMiningMust selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UsersMiningMust record,
                                 @Param("example") UsersMiningMustExample example);

    int updateByExample(@Param("record") UsersMiningMust record, @Param("example") UsersMiningMustExample example);

    int updateByPrimaryKeySelective(UsersMiningMust record);

    int updateByPrimaryKey(UsersMiningMust record);

    /**
     * 根据uid及status状态查询
     *
     * @param uid
     * @param status
     * @return
     */
    UsersMiningMust selectByUid(@Param("uid") Long uid, @Param("status") int status);
}
