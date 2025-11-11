package com.erban.admin.main.service;

import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.UsersWithdrawWhitelistDTO;
import com.erban.main.model.Users;
import com.erban.main.model.UsersWithdrawWhitelist;
import com.erban.main.model.UsersWithdrawWhitelistExample;
import com.erban.main.mybatismapper.UsersWithdrawWhitelistMapper;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UsersWithdrawWhitelistService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersWithdrawWhitelistMapper usersWithdrawWhitelistMapper;

    @Autowired
    private JedisService jedisService;

    public PageInfo getList(Integer pageNumber, Integer pageSize, Long erbanNo) {
        PageHelper.startPage(pageNumber,pageSize);
        Map<String, Object> map = new HashMap<>(16);
        if(erbanNo!=null){
            map.put("erbanNo", erbanNo);
        }
        List<UsersWithdrawWhitelistDTO> usersWithdrawWhitelistDTOS = usersWithdrawWhitelistMapper.selectByParam(map);
        return new PageInfo(usersWithdrawWhitelistDTOS);
    }

    public BusiResult add(Long erbanNo, int adminId) {
        Users users=usersService.getUsresByErbanNo(erbanNo);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        UsersWithdrawWhitelistExample example = new UsersWithdrawWhitelistExample();
        example.createCriteria().andUidEqualTo(users.getUid());
        List<UsersWithdrawWhitelist> usersWithdrawWhitelists = usersWithdrawWhitelistMapper.selectByExample(example);
        if(usersWithdrawWhitelists.size() > 0){
            return new BusiResult(BusiStatus.USERS_WITHDRAW_WHITELIST_ISEXISTS_ERROR);
        }
        UsersWithdrawWhitelist usersWithdrawWhitelist = new UsersWithdrawWhitelist();
        usersWithdrawWhitelist.setUid(users.getUid());
        usersWithdrawWhitelist.setAdminId(adminId);
        usersWithdrawWhitelist.setCreateTime(new Date());
        usersWithdrawWhitelist.setStatus(1);
        int status = usersWithdrawWhitelistMapper.insert(usersWithdrawWhitelist);
        if(status > 0){
            Gson gson = new Gson();
            jedisService.hset(RedisKey.users_withdraw_whitelist.getKey(), String.valueOf(users.getUid()), gson.toJson(usersWithdrawWhitelist));
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    public BusiResult delete(Integer id) {
        UsersWithdrawWhitelist usersWithdrawWhitelist = usersWithdrawWhitelistMapper.selectByPrimaryKey(id);
        int status = usersWithdrawWhitelistMapper.deleteByPrimaryKey(id);
        if(status > 0){
            jedisService.hdel(RedisKey.users_withdraw_whitelist.getKey(), String.valueOf(usersWithdrawWhitelist.getUid()));
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    public BusiResult setting(Integer id) {
        UsersWithdrawWhitelist usersWithdrawWhitelist = usersWithdrawWhitelistMapper.selectByPrimaryKey(id);
        if(usersWithdrawWhitelist.getStatus() == 1){
            usersWithdrawWhitelist.setStatus(0);
            int status = usersWithdrawWhitelistMapper.updateByPrimaryKeySelective(usersWithdrawWhitelist);
            if(status > 0){
                jedisService.hdel(RedisKey.users_withdraw_whitelist.getKey(), String.valueOf(usersWithdrawWhitelist.getUid()));
            }
            return new BusiResult(BusiStatus.SUCCESS);
        }else{
            usersWithdrawWhitelist.setStatus(1);
            int status = usersWithdrawWhitelistMapper.updateByPrimaryKeySelective(usersWithdrawWhitelist);
            if(status > 0){
                Gson gson = new Gson();
                jedisService.hset(RedisKey.users_withdraw_whitelist.getKey(), String.valueOf(usersWithdrawWhitelist.getUid()), gson.toJson(usersWithdrawWhitelist));
            }
            return new BusiResult(BusiStatus.SUCCESS);
        }
    }
}
