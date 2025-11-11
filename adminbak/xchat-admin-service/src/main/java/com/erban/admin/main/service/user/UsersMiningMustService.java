package com.erban.admin.main.service.user;

import com.erban.admin.main.dto.UsersMiningMustDTO;
import com.erban.admin.main.mapper.UsersMiningMustMgr;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.model.Users;
import com.erban.main.model.UsersMiningMust;
import com.erban.main.mybatismapper.UsersMiningMustMapper;
import com.erban.main.mybatismapper.UsersMiningMustCopyMapper;
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

/**
 * @author chris
 * @Title:
 * @date 2018/10/15 15:37
 */
@Service
public class UsersMiningMustService {
    @Autowired
    private UsersMiningMustMgr usersMiningMustMgr;

    @Autowired
    private UsersMiningMustMapper usersMiningMustMapper;

    @Autowired
    private UsersMiningMustCopyMapper usersMiningMustCopyMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<UsersMiningMustDTO> getList(Integer pageNumber, Integer pageSize, Long erbanNo,
                                                String startDateStr, String endDateStr) {
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> map = new HashMap<>(16);
        if (erbanNo != null) {
            map.put("erbanNo", erbanNo);
        }

        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            startDateStr = startDateStr + " 00:00:00";
            endDateStr = endDateStr + " 23:59:59";
            map.put("startDateStr", startDateStr);
            map.put("endDateStr", endDateStr);
        }

        List<UsersMiningMustDTO> usersMiningMustDTOS = usersMiningMustMgr.selectByParam(map);
        usersMiningMustDTOS.stream().forEach(item -> {
            if (item.getAdminId() != null) {
                item.setAdminName(adminUserService.getAdminUserById(item.getAdminId()).getUsername());
            } else {
                item.setAdminName("未知");
            }
        });

        return new PageInfo<>(usersMiningMustDTOS);
    }

    /**
     * 投放全服必中记录
     *
     * @param erbanNo 拉贝号
     * @param giftId  礼物ID
     * @param adminId 管理员ID
     * @return
     */
    public BusiResult addUsersMiningMust(Long erbanNo, Integer giftId, Integer adminId, Long inputGold, Long outputGold, Double rate) {
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }

        UsersMiningMust usersMiningMust = usersMiningMustMapper.selectByUid(users.getUid(), 1);
        if (usersMiningMust == null) {
            usersMiningMust = new UsersMiningMust();
            usersMiningMust.setUid(users.getUid());
            usersMiningMust.setCreateTime(new Date());
            usersMiningMust.setStatus(1);
            usersMiningMust.setGiftId(giftId);
            usersMiningMust.setInputGold(inputGold);
            usersMiningMust.setOutputGold(outputGold);
            usersMiningMust.setRate(rate);
            usersMiningMust.setAdminId(adminId);
            if (adminId < 1000 || 10000 < adminId) {
                int status = usersMiningMustMapper.insert(usersMiningMust);
                if (status > 0) {
                    Gson gson = new Gson();
                    jedisService.hset(RedisKey.users_mining_must.getKey(), users.getUid().toString(),
                            gson.toJson(usersMiningMust));
                }
            } else {
                int status = usersMiningMustCopyMapper.insert(usersMiningMust);
                if (status > 0) {
                    Gson gson = new Gson();
                    jedisService.hset(RedisKey.users_mining_must.getKey(), users.getUid().toString(),
                            gson.toJson(usersMiningMust));
                }
            }

            return new BusiResult(BusiStatus.SUCCESS);
        } else {
            return new BusiResult(BusiStatus.USERS_EXISTED_ERROR);
        }
    }

    /**
     * 根据礼物ID查询该礼物名称
     *
     * @param giftId 礼物ID
     * @return
     */
    public String queryGiftName(Integer giftId) {
        return usersMiningMustMgr.selectGiftName(giftId);
    }
}
