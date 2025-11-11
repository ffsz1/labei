package com.xchat.oauth2.service.service.account;

import com.xchat.common.RegexUtil;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.PrettyErbanNoMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.PrettyErbanNo;
import com.xchat.oauth2.service.model.PrettyErbanNoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ErBanNoService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PrettyErbanNoMapper prettyErbanNoMapper;

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
                throw new Exception(GlobalConfig.appName + "号生成异常");
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
                throw new Exception(GlobalConfig.appName + "号生成异常");
            }
            isPrettyFilter=true;
        }
        return Long.valueOf(generalId);
    }

    private boolean isExsistErbanAccount(Long erbanNo) throws Exception {
        boolean flag = false;
        Account account = accountService.getAccountByErBanNo(erbanNo);
        if (account != null) {
            flag = true;
        }
        return flag;
    }

    public Long generalId(int digit) throws Exception {
        String generalId = CommonUtil.getRandomNumStr(digit);
        boolean flag = false;
        do {
            flag = RegexUtil.checkPretty(generalId);
            if (!flag) {
                generalId = CommonUtil.getRandomNumStr(digit);
            }
        } while (!flag);
        return Long.valueOf(generalId);
    }

    public static void main(String args[]) throws Exception {

//        6766696
//        6665807
        ErBanNoService erBanNoService = new ErBanNoService();
        for (int i = 0; i < 10000; i++) {
            System.out.println(erBanNoService.generalNotPrettyId(7));
        }
    }

    public Boolean isExistPrettyNo(Long erbanNo) throws Exception {
        PrettyErbanNoExample prettyErbanNoExample = new PrettyErbanNoExample();
        prettyErbanNoExample.createCriteria().andPrettyErbanNoEqualTo(erbanNo);
        List<PrettyErbanNo> prettyErbanNoList = prettyErbanNoMapper.selectByExample(prettyErbanNoExample);
        if (CollectionUtils.isEmpty(prettyErbanNoList)) {
            return false;
        } else {
            return true;
        }
    }


}
