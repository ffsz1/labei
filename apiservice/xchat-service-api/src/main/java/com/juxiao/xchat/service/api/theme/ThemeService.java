package com.juxiao.xchat.service.api.theme;

import com.juxiao.xchat.dao.theme.domain.Theme;
import com.juxiao.xchat.dao.theme.query.ThemeQuery;
import com.juxiao.xchat.service.api.theme.bo.ThemeBo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;

import java.util.List;

public interface ThemeService {

    int add(ThemeBo themeBo);

    int deleteById(Long id);

    int updateSelect(ThemeBo themeBo);

    Theme getTheme(Long id);

    /**
     * 分页查询主题
     * @param themeQuery 查询条件
     * @param pageBo 分页参数
     * @return 主题列表
     */
    List<Theme> listTheme(ThemeQuery themeQuery, PageBo pageBo);

    List<Theme> findHot(PageBo pageBo);
}
