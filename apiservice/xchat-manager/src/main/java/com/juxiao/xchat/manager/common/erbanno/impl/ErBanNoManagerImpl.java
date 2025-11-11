package com.juxiao.xchat.manager.common.erbanno.impl;

import com.juxiao.xchat.base.utils.CommonUtil;
import com.juxiao.xchat.base.utils.RegexUtil;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.manager.common.erbanno.ErBanNoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErBanNoManagerImpl implements ErBanNoManager {
    @Autowired
    private AccountDao accountDao;

    //TODO 待改善，多台机器部署时候应该采用雪花算法，防止重复
    public Long getErBanNo() throws Exception {
        int digit = 7;
        Long erbanNo = generalNotPrettyId(digit);
        int num = 0;
        while (isExsistErbanAccount(erbanNo)) {
            num++;
            erbanNo = generalNotPrettyId(digit);
            if (num == 3) {//3次未生成，升级位数
                digit++;
            } else if (num == 6) {//6次未生成，继续升级位数
                digit++;
            } else if (num == 10) {
                throw new Exception("用户号生成异常");
            }
        }
        return erbanNo;

    }


    public Long generalNotPrettyId(int digit) throws Exception {
        String generalId = "";
        //判断是否靓号
//        boolean flag = isExistPrettyNo(generalId);
        boolean isPrettyFilter = true;
        int numFilter = 0;
        int condition = 5;
        while (isPrettyFilter) {
            numFilter++;
            generalId = CommonUtil.getRandomNumStr(digit);
            boolean dumpNumber = CommonUtil.checkMaxDumpNumber(generalId, condition);
            isPrettyFilter = RegexUtil.checkPretty(generalId);
            if (!isPrettyFilter && !dumpNumber) {// 不是靓号，并且不同的数字超过condition
                break;
            }
            System.out.println("morethan=" + generalId + "&dumpNumber=" + dumpNumber + "&isPrettyFilter=" + isPrettyFilter);
            if (numFilter == 3) {//3次生成都是过滤的靓号，不同数减少/升级位数
                System.out.println("morethan 3=" + generalId + "&dumpNumber=" + dumpNumber);
//                digit++;
                condition--;
            } else if (numFilter == 6) {//6次未生成，不同数减少/继续升级位数
                System.out.println("morethan 6=" + generalId + "&dumpNumber=" + dumpNumber);
//                digit++;
                condition--;
            } else if (numFilter == 10) {
                throw new Exception("用户号生成异常");
            }
            isPrettyFilter=true;
        }
        return Long.valueOf(generalId);
    }

    private boolean isExsistErbanAccount(Long erbanNo) throws Exception {
        boolean flag = false;
        List<AccountDTO> account = accountDao.selectByErbanNo(erbanNo);
        if (account != null&&account.size()>0) {
            flag = true;
        }
        return flag;
    }
}
