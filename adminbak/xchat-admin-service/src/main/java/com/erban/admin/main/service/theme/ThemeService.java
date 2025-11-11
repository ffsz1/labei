package com.erban.admin.main.service.theme;


import com.erban.main.model.Theme;
import com.erban.main.mybatismapper.ThemeMapper;
import com.erban.main.util.StringUtils;
import com.erban.main.wechat.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThemeService {

    @Autowired
    private ThemeMapper themeMapper;

    public PageInfo<Theme> getListWithPage(String state,int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Theme> list = themeMapper.selectByCondition(state);
        return new PageInfo<>(list);
    }

    public int delete(Long[] toArray) {
        int count=0;
        for(Long item:toArray){
            int i = themeMapper.deleteByPrimaryKey(item);
            count+=i;
        }
        return count;
    }

    public Theme get(Long id) {
        return themeMapper.selectByPrimaryKey(id);
    }

    public int save(Theme theme) {
        Date date=new Date();
        theme.setUpdateTime(date);
        if(theme.getId()!=null) {
            Theme themeget = get(theme.getId());
            if(themeget==null) return 0;
            return themeMapper.updateByPrimaryKeySelective(theme);
        }else{
            if(theme.getName()==null||theme.getPictureUrl()==null|| StringUtils.isEmpty(theme.getRemarks()))
                return 0;
            theme.setCreateTime(date);
            return themeMapper.insertSelective(theme);
        }
    }
}
