package com.erban.admin.main.service.account;

import com.erban.admin.main.service.charge.ChargeRecordAdminService;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.*;
import com.erban.main.service.ChargeRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.vo.admin.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 注册管理service
 */
@Service
public class AccountAdminService {
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 提现列表 查询+分页
     *
     * @param
     * @return
     */
    public BusiResult getList(AccountParam accountParam) {
        PageHelper.startPage(accountParam.getPage(), accountParam.getSize());
        if (StringUtils.isNotBlank(accountParam.getBeginDate())) {
            accountParam.setBeginDate(accountParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(accountParam.getEndDate())) {
            accountParam.setEndDate(accountParam.getEndDate() + " 23:59:59");
        }
        List<AccountVo> accountVos = accountMapper.selectByQuery(accountParam);
        if (accountVos == null || accountVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }
        //获取此条件下的男女人数
        List<AccountVo> sumNum = accountMapper.seleNumByQuery(accountParam);
        AccountVo accountVo = accountVos.get(0);
        for (AccountVo vo : sumNum) {
            if (vo.getGender().toString().equals("1")) {
                accountVo.setMaleNum(vo.getSumNum());
            } else if (vo.getGender().toString().equals("2")) {
                accountVo.setFemaleNum(vo.getSumNum());
            } else if (vo.getGender().toString().equals("0")) {
                accountVo.setOther(vo.getSumNum());
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(accountVos));
    }

    /**
     * 导出
     *
     * @param
     * @return
     */
    public List<AccountVo> exportList(AccountParam accountParam) {
        if (StringUtils.isNotBlank(accountParam.getBeginDate())) {
            accountParam.setBeginDate(accountParam.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(accountParam.getEndDate())) {
            accountParam.setEndDate(accountParam.getEndDate() + " 23:59:59");
        }
        List<AccountVo> accountVos = accountMapper.selectByQuery(accountParam);
        //获取此条件下的男女人数
        List<AccountVo> sumNum = accountMapper.seleNumByQuery(accountParam);
        AccountVo accountVo = accountVos.get(0);
        for (AccountVo vo : sumNum) {
            if (vo.getGender().toString().equals("1")) {
                accountVo.setMaleNum(vo.getSumNum());
            } else if (vo.getGender().toString().equals("2")) {
                accountVo.setFemaleNum(vo.getSumNum());
            } else if (vo.getGender().toString().equals("0")) {
                accountVo.setOther(vo.getSumNum());
            }
        }

        return accountVos;
    }
}
