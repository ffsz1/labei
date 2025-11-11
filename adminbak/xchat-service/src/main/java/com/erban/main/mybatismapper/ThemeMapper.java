package com.erban.main.mybatismapper;

import com.erban.main.model.Theme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThemeMapper {

    //删除
    int deleteByPrimaryKey(Long id);

    //插入
    int insert(Theme record);

    //选择性插入
    int insertSelective(Theme record);

    //根据主键查询
    Theme selectByPrimaryKey(Long id);
    //选择性更新
    int updateByPrimaryKeySelective(Theme record);

    //更新
    int updateByPrimaryKey(Theme record);

    List<Theme> selectByCondition(@Param("state") String state);

    /**
     * 关联性删除 话题 动态 评论 回复
     * @param id 话题id
     * @return
     */
    int deleteByIdWithRelative(Long id);
}