package com.erban.main.mybatismapper;

import com.erban.main.model.UserPurseSurplus;
import com.erban.main.model.UserPurseSurplusExample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/9
 * @time 16:24
 */
public interface UserPurseSurplusMapper {

    int countByExample(UserPurseSurplusExample example);

    int deleteByExample(UserPurseSurplusExample example);

    int deleteByPrimaryKey(Date surplusDate);

    int insert(UserPurseSurplus record);

    int insertSelective(UserPurseSurplus record);

    List<UserPurseSurplus> selectByExample(UserPurseSurplusExample example);

    UserPurseSurplus selectByPrimaryKey(Date surplusDate);

    int updateByExampleSelective(@Param("record") UserPurseSurplus record, @Param("example") UserPurseSurplusExample example);

    int updateByExample(@Param("record") UserPurseSurplus record, @Param("example") UserPurseSurplusExample example);

    int updateByPrimaryKeySelective(UserPurseSurplus record);

    int updateByPrimaryKey(UserPurseSurplus record);

    List<UserPurseSurplus> listByParam(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
}
