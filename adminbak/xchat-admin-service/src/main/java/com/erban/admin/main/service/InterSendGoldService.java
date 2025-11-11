package com.erban.admin.main.service;

import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterSendGoldService {

    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private UsersService usersService;

    public BusiResult intelSendGold(Long erbanNo, Long goldNum) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsersByErBanNo(erbanNo);
        UserPurse userPurse = userPurseService.getPurseByUid(users.getUid());
        if (userPurse == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
        billRecordService.insertBillRecord(users.getUid(), users.getUid(), null, Constant.BillType.interSendGold, null, goldNum, null);
        userPurseService.updateUserPurse(userPurse);
        return busiResult;
    }

    public BusiResult intelSendGoldList(Long[] uids, Long goldNum) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        for (Long uid : uids) {
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            if (userPurse == null) {
                return new BusiResult(BusiStatus.BUSIERROR);
            }
            userPurse.setGoldNum(userPurse.getGoldNum() + goldNum);
            billRecordService.insertBillRecord(uid, uid, null, Constant.BillType.interSendGold, null, goldNum, null);
        }
        return busiResult;
    }
}
