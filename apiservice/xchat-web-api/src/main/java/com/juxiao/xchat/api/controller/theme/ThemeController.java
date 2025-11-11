package com.juxiao.xchat.api.controller.theme;


import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.theme.domain.Theme;
import com.juxiao.xchat.dao.theme.query.ThemeQuery;
import com.juxiao.xchat.service.api.theme.ThemeService;
import com.juxiao.xchat.service.api.theme.bo.ThemeBo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    private ThemeService themeService;
    @RequestMapping("/add")
    public WebServiceMessage add(ThemeBo themeBo){
        int result = themeService.add(themeBo);
        System.out.println(result);
        if(result==0) return new WebServiceMessage(505,"保存失败");
        return new WebServiceMessage(200,result,"保存成功");
    }
    @RequestMapping("/delete")
    public WebServiceMessage delete(Long id){
        int result = themeService.deleteById(id);
        System.out.println(result);
        if(result==0) return new WebServiceMessage(505,"操作失败");
        return new WebServiceMessage(200,result,"操作成功");
    }

    @RequestMapping("/update")
    public WebServiceMessage update(ThemeBo themeBo){
        int result = themeService.updateSelect(themeBo);
        System.out.println(result);
        if(result==-1) return new WebServiceMessage(505,"操作失败");
        return new WebServiceMessage(200,result,"操作成功");
    }

    @RequestMapping("/get")
    public WebServiceMessage get(Long  id){
        Theme theme = themeService.getTheme(id);
        return new WebServiceMessage(200,theme,"查询成功");
    }

    @RequestMapping("/listPage")
    public WebServiceMessage listPage(ThemeQuery themeQuery, PageBo pageBo){
        String message = pageBo.checkParam();
        if(message !=null) return new WebServiceMessage(505,message);
        themeQuery.setState("1");
        List<Theme> themes = themeService.listTheme(themeQuery, pageBo);
        return WebServiceMessage.success(themes);
    }

    @RequestMapping("/tophot")
    public WebServiceMessage tophot(PageBo pageBo){
        String s = pageBo.checkParam();
        if(s!=null) return WebServiceMessage.failure(s);
        List<Theme> themes = themeService.findHot(pageBo);
        return WebServiceMessage.success(themes);
    }

}
