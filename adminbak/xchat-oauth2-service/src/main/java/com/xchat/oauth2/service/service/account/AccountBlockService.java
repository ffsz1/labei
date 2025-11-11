package com.xchat.oauth2.service.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuguofu on 2017/12/8.
 */
@Service
public class AccountBlockService {
    @Autowired
    private NetEaseService netEaseService;

    public void checkAccountBlock(){

    }

    /**
     * 封禁用户账号、
     * 1、记录封禁业务数据之后调用该接口
     * 2、调用云信，封禁用户账号的同时，将用户踢下线
     * 3、恢复账号封禁，账号登录判断依赖业务数据
     * @param uid
     * @throws Exception
     */
    public void doAccountBlock(Long uid) throws Exception{
        String accid=uid.toString();
        netEaseService.block(accid,"true");//调用云信封禁账号功能，主要用于踢用户下线

        netEaseService.unblock(accid);
    }

}
