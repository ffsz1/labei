package com.erban.admin.main.service.system;

import com.erban.admin.main.mapper.AdminDictMapper;
import com.erban.admin.main.model.AdminDict;
import com.erban.admin.main.model.AdminDictExample;
import com.erban.admin.main.model.AdminDictKey;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("adminDictService")
public class AdminDictService {
    @Autowired
    private AdminDictMapper adminDictMapper;


    /**
     * 分页查询
     *
     * @param code 名称
     * @param page
     * @param size
     * @return
     */
    public PageInfo getDictByPage(String code, int page, int size) {
        AdminDictExample example = new AdminDictExample();
        if (!BlankUtil.isBlank(code)) {
            example.createCriteria().andCodeEqualTo(code);
        }
        PageHelper.startPage(page, size);
        List<AdminDict> list = adminDictMapper.selectByExample(example);
        return new PageInfo(list);
    }

    public int saveDict(AdminDict adminDict, boolean isEdit) {
        if (isEdit) {
            return adminDictMapper.updateByPrimaryKey(adminDict);
        } else {
            adminDict.setCreatetime(new Date());
            return adminDictMapper.insert(adminDict);
        }
    }

    public AdminDict getOneAdminDict(String code, String dictkey) {
        AdminDictKey adminDictKey = new AdminDictKey();
        adminDictKey.setCode(code);
        adminDictKey.setDictkey(dictkey);
        return adminDictMapper.selectByPrimaryKey(adminDictKey);
    }

    public void delAdminDict(String code, String dictkey) {
        AdminDictKey adminDictKey = new AdminDictKey();
        adminDictKey.setCode(code);
        adminDictKey.setDictkey(dictkey);
        adminDictMapper.deleteByPrimaryKey(adminDictKey);
    }

    public List<AdminDict> getDictByCode(String code) {
        AdminDictExample example = new AdminDictExample();
        example.createCriteria().andCodeEqualTo(code);
        return adminDictMapper.selectByExample(example);
    }

}
