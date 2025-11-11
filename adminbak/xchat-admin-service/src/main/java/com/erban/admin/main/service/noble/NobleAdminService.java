package com.erban.admin.main.service.noble;

import com.erban.admin.main.mapper.AdminLogMapper;
import com.erban.admin.main.model.AdminLog;
import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.noble.NoblePayService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class NobleAdminService extends BaseService{
    private static final Logger logger = LoggerFactory.getLogger(NobleAdminService.class);
    @Autowired
    private NobleRightService nobleRightService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private NobleUsersService nobleUsersService;

    @Autowired
    private AdminLogMapper adminLogMapper;

    @Autowired
    private NoblePayService noblePayService;

    @Autowired
    private UsersMapper usersMapper;


    public List<NobleRight> getNobleRight() {
        List<NobleRight> nobleRightList = nobleRightService.getNobleRightList();
        if(!CollectionUtils.isEmpty(nobleRightList)){
            return  nobleRightList;
        }
        return null;
    }

    public PageInfo<Users> getNobleUser(Long erbanNo) {
        Users user = usersService.getUsersByErBanNo(erbanNo);
        List<Users> list =new ArrayList<>();
        list.add(user);
        return new PageInfo<>(list);
    }


public int openNoble(Long erbanNo,int nobleId,int adminId) {
    try{
        Long uid = erbanNoToUid(String.valueOf(erbanNo));
        if(uid==0){
            return 2;//拉贝号不存在
        }
        NobleUsers nobleUser = nobleUsersService.getNobleUser(uid);
        if(!StringUtils.isEmpty(nobleUser)){
            return 2;//该用户已经开通了贵族
        }else{
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);
            noblePayService.handlOpenOrRenewNoble(uid, null, Constant.BillType.openNoble,(byte)1,(byte)3, nobleRight);
            AdminLog adminlog =new AdminLog();
            adminlog.setCreateTime(new Date());
            adminlog.setOptUid(adminId);
            adminlog.setOptClass(NobleAdminService.class.getName());
            adminLogMapper.insert(adminlog);
            return 1;
            }
        }catch (Exception e){
            logger.error("openNobleByGold is error",e);
        }
        return 0;
    }

public int renewNoble(Long erbanNo,int nobleId,int adminId) {
    try{
        Long uid = erbanNoToUid(String.valueOf(erbanNo));
        NobleUsers nobleUser = nobleUsersService.getNobleUser(uid);
        if(!StringUtils.isEmpty(nobleUser)&& nobleId==nobleUser.getNobleId()){
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleId);
            noblePayService.handlOpenOrRenewNoble(uid, null, Constant.BillType.renewNoble,(byte)2,(byte)3, nobleRight);
            AdminLog adminlog =new AdminLog();
            adminlog.setCreateTime(new Date());
            adminlog.setOptUid(adminId);
            adminlog.setOptClass(NobleAdminService.class.getName());
            adminLogMapper.insert(adminlog);
            return 1;
            }else{
            return 2;
            }
        }catch (Exception e){
        logger.error("renew is error",e);
        }
        return 0;
    }


private Long erbanNoToUid(String erBanNo) {
        Long uid =0L;
        if (!StringUtils.isEmpty(erBanNo)) {
            UsersExample example = new UsersExample();
            long longUid = Long.parseLong(erBanNo);
            example.createCriteria().andErbanNoEqualTo(longUid);
            List<Users> usersList = usersMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(usersList)) {
                uid = usersList.get(0).getUid();
            }
        }else{
            logger.error("param is error");
        }
        return uid;
    }

}
