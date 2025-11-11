package com.juxiao.xchat.service.api.user;

public interface UsersUniversalService {


    /**
     * 检测用户是否在提现白名单中
     * @param uid uid
     * @return boolean
     */
    boolean checkUsersWithdrawWhitelist(Long uid);

}
