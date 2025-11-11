package com.erban.main.mybatismapper;

import com.erban.main.model.SysConf;
import com.erban.main.model.SysConfExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysConfMapper {
    int deleteByExample(SysConfExample example);

    int deleteByPrimaryKey(String configId);

    int insert(SysConf record);

    int insertSelective(SysConf record);

    List<SysConf> selectByExample(SysConfExample example);

    SysConf selectByPrimaryKey(String configId);

    int updateByExampleSelective(@Param("record") SysConf record, @Param("example") SysConfExample example);

    int updateByExample(@Param("record") SysConf record, @Param("example") SysConfExample example);

    int updateByPrimaryKeySelective(SysConf record);

    int updateByPrimaryKey(SysConf record);

    int countByExample(SysConfExample example);
}
