package com.erban.admin.main.service.user;

import com.erban.admin.main.mapper.TestPurseMapper;
import com.erban.admin.main.model.TestPurse;
import com.erban.admin.main.model.TestPurseExample;
import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.UserPurse;
import com.erban.main.model.UserPurseExample;
import com.erban.main.mybatismapper.UserPurseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserPurseAdminService extends BaseService {

    @Autowired
    private TestPurseMapper testPurseMapper;
    @Autowired
    private UserPurseMapper userPurseMapper;

    public PageInfo<UserPurse> getPurseByPage(int page, int size){
        PageHelper.startPage(page, size);
        UserPurseExample example = new UserPurseExample();
        List<UserPurse> list = userPurseMapper.selectByExample(example);
        return new PageInfo(list);
    }

    public int addTestPurse(TestPurse testPurse){
        testPurse.setCreateTime(new Date());
        return testPurseMapper.insertSelective(testPurse);
    }

    public int clearTestPurseDb() {
        return testPurseMapper.deleteByExample(new TestPurseExample());
    }
}
