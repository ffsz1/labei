package com.erban.admin.main.service.account;

import com.erban.admin.main.service.base.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountLoginRecordMapper;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.model.AccountLoginRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@Service
public class AccountLoginRecordService extends BaseService {
    @Autowired
    AccountLoginRecordMapper  accountLoginRecordMapper;

    public PageInfo<AccountLoginRecord> getAccountLoginRecordList(Integer pageSize, Integer pageNum,Long erbanNo, String phone,Byte loginType){
        AccountLoginRecordExample accountLoginRecordExample =new AccountLoginRecordExample();
        accountLoginRecordExample.setOrderByClause(" create_time desc ");
        if (loginType != 0) {
            if (!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(erbanNo)) {
                accountLoginRecordExample.createCriteria().andPhoneEqualTo(phone).andLoginTypeEqualTo(loginType).andErbanNoEqualTo(erbanNo);
            } else if(!StringUtils.isEmpty(phone) && StringUtils.isEmpty(erbanNo)){
                accountLoginRecordExample.createCriteria().andLoginTypeEqualTo(loginType).andPhoneEqualTo(phone);
            } else if(!StringUtils.isEmpty(erbanNo) && StringUtils.isEmpty(phone)){
                accountLoginRecordExample.createCriteria().andLoginTypeEqualTo(loginType).andErbanNoEqualTo(erbanNo);
            } else{
                accountLoginRecordExample.createCriteria().andLoginTypeEqualTo(loginType);
            }
        } else {
            if (!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(erbanNo)) {
                accountLoginRecordExample.createCriteria().andPhoneEqualTo(phone).andErbanNoEqualTo(erbanNo);
            }else if(!StringUtils.isEmpty(phone) && StringUtils.isEmpty(erbanNo)){
                accountLoginRecordExample.createCriteria().andPhoneEqualTo(phone);
            }else if(!StringUtils.isEmpty(erbanNo) && StringUtils.isEmpty(phone)){
                accountLoginRecordExample.createCriteria().andErbanNoEqualTo(erbanNo);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<AccountLoginRecord> accountLoginRecord = accountLoginRecordMapper.selectByExample(accountLoginRecordExample);
        return new PageInfo<>(accountLoginRecord);
    }
}
