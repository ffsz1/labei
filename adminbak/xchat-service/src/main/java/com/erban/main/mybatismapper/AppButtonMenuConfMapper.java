package com.erban.main.mybatismapper;

import com.erban.main.model.AppButtonMenuConf;
import com.erban.main.model.AppButtonMenuConfExample;
import java.util.List;

public interface AppButtonMenuConfMapper {
    int deleteByPrimaryKey(Integer confId);

    int insert(AppButtonMenuConf record);

    int insertSelective(AppButtonMenuConf record);

    List<AppButtonMenuConf> selectByExample(AppButtonMenuConfExample example);

    AppButtonMenuConf selectByPrimaryKey(Integer confId);

    int updateByPrimaryKeySelective(AppButtonMenuConf record);

    int updateByPrimaryKey(AppButtonMenuConf record);
}
