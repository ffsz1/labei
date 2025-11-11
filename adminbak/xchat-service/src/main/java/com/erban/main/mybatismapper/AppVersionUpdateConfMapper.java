package com.erban.main.mybatismapper;

import com.erban.main.model.AppVersionUpdateConf;
import com.erban.main.model.AppVersionUpdateConfExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppVersionUpdateConfMapper {
    int deleteByExample(AppVersionUpdateConfExample example);

    int deleteByPrimaryKey(String version);

    int insert(AppVersionUpdateConf record);

    int insertSelective(AppVersionUpdateConf record);

    List<AppVersionUpdateConf> selectByExample(AppVersionUpdateConfExample example);

    AppVersionUpdateConf selectByPrimaryKey(String version);

    int updateByExampleSelective(@Param("record") AppVersionUpdateConf record, @Param("example") AppVersionUpdateConfExample example);

    int updateByExample(@Param("record") AppVersionUpdateConf record, @Param("example") AppVersionUpdateConfExample example);

    int updateByPrimaryKeySelective(AppVersionUpdateConf record);

    int updateByPrimaryKey(AppVersionUpdateConf record);
}
