package com.juxiao.xchat.service.api.theme.impl;

import com.juxiao.xchat.dao.theme.ThemeDao;
import com.juxiao.xchat.dao.theme.domain.Theme;
import com.juxiao.xchat.dao.theme.query.ThemeQuery;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.service.api.theme.ThemeService;
import com.juxiao.xchat.service.api.theme.bo.ThemeBo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeDao themeDao;

    @Override
    public int add(ThemeBo themeBo) {
        if(StringUtils.isEmpty(themeBo.getName())) return 0;
        Theme theme=themeBo.getTheme();
        Date date=new Date();
        theme.setCreateTime(date);
        theme.setUpdateTime(date);
        return themeDao.insertSelective(theme);
    }

    @Override
    public int deleteById(Long id) {
        if(id==null) return 0;
        return themeDao.deleteByPrimaryKey(id);
    }

    @Override
    public int updateSelect(ThemeBo themeBo) {
        if(themeBo.getId()==null|| StringUtils.isEmpty(themeBo.getName())) return -1;
        Theme theme = themeBo.getTheme();
        Date date=new Date();
        theme.setUpdateTime(date);
        return themeDao.updateByPrimaryKeySelective(theme);
    }

    @Override
    public Theme getTheme(Long id) {
        if(id==null) return null;
        return themeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Theme> listTheme(ThemeQuery themeQuery, PageBo pageBo) {

        Long size = themeDao.getSize(themeQuery);
        if(size==0) return new ArrayList<Theme>();
        Page page = PageBo.getPage(pageBo);
        List<Theme> themes = themeDao.listTheme(themeQuery, page);
        //PageInfo<Theme> pageInfo=new PageInfo<>(pageBo,size,themes);
        return themes;
    }

    @Override
    public List<Theme> findHot(PageBo pageBo) {
        Page page = PageBo.getPage(pageBo);
        if(page==null) return new ArrayList<Theme>();
        List<Theme> themes = themeDao.findTopHot(page);
        return themes;
    }


}
