package com.juxiao.xchat.dao.theme;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.juxiao.xchat.dao.theme.domain.Theme;
import com.juxiao.xchat.dao.theme.query.ThemeQuery;
import com.juxiao.xchat.dao.wish.query.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThemeDao {

    //删除
    int deleteByPrimaryKey(Long id);

    //插入
    int insert(Theme record);

    //选择性插入
    int insertSelective(Theme record);

    //根据主键查询
    Theme selectByPrimaryKey(Long id);

    /**
     * 分页查询主题
     * @param themeQuery 查询条件
     * @param page 分页条件
     * @return 页面数据
     */
    List<Theme> listTheme(@Param("query") ThemeQuery themeQuery, @Param("page") Page page);

    Long getSize(@Param("query") ThemeQuery themeQuery);

    //选择性更新
    int updateByPrimaryKeySelective(Theme record);

    //更新
    int updateByPrimaryKey(Theme record);

    //按话题参与量降序排序
    List<Theme> findTopHot(@Param("page") Page bottom);

    void addNum(Long themeid);
}