package com.erban.main.service;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopstarsService {
    private static final Logger logger = LoggerFactory.getLogger(PopstarsService.class);

    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;

    public BusiResult followSendGold(String user, String openId, Byte wxGender) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        boolean isExists=usersService.checkWxPubFansOpenidExists(openId);
        if(isExists){
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("已经领取过金币了!");
            return busiResult;
        }
        boolean flag = CommonUtil.checkValidPhone(user);
        Users users = null;
        if (flag) {
            logger.info("用户关注公众号送金币，phone：" + user);
            //手机号
            users = usersService.getUsersByPhone(user);
        } else {
            logger.info("用户关注公众号送金币,erban_no:" + user);
            //拉贝号
            Long erBanNo = Long.valueOf(user);
            users = usersService.getUsersByErBanNo(erBanNo);
        }
        if (users == null || users.getWxPubFansOpenid() != null || users.getWxPubFansGender() != null) {
            logger.info("用户关注公众号送金币错误，uid:" + users.getUid());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        users.setWxPubFansGender(wxGender);
        users.setWxPubFansOpenid(openId);
        usersService.updateWxOpenIdAndGender(users);
        logger.info("用户关注公众号送金币:更新用户的OpenId:" + openId + ",wxGender:" + wxGender);
        UserPurse userPurse = userPurseService.getPurseByUid(users.getUid());
        if (userPurse == null) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        // 赠送金币，增加钱包中普通金币和总金币的数量
        int result = userPurseUpdateService.addChargeGoldDbAndCache(users.getUid(), Constant.SendGold.followWX);
        logger.info("addChargeGoldDbAndCache result:{}, uid:{}, goldNum:{}", result, users.getUid(), Constant.SendGold.followWX);
        billRecordService.insertBillRecord(users.getUid(), users.getUid(), null, Constant.BillType.followSendGold
                , null, Constant.SendGold.followWX, null);
        return busiResult;
    }

    public BusiResult getErBanUser(String user) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        boolean flag = CommonUtil.checkValidPhone(user);
        Users users = null;
        if (flag) {
            logger.info("用户关注公众号送金币,通过手机号获取用户资料，phone：" + user);
            //手机号
            users = usersService.getUsersByPhone(user);
        } else {
            logger.info("用户关注公众号送金币,通过" + GlobalConfig.appName + "号获取用户资料,erban_no:" + user);
            //拉贝号
            Long erBanNo = Long.valueOf(user);
            users = usersService.getUsersByErBanNo(erBanNo);
        }
        if (users == null) {
            logger.info("用户输入" + GlobalConfig.appName + "号或手机号查找的账户不存在.user:" + user);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        UserVo userVo = new UserVo();
        userVo.setErbanNo(users.getErbanNo());
        userVo.setNick(users.getNick());
        busiResult.setData(userVo);
        return busiResult;
    }
}
