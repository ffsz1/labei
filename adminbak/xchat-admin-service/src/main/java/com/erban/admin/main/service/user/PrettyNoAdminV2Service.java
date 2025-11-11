package com.erban.admin.main.service.user;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.UserDrawPrettyErbanNo;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.UserDrawPrettyErbanNoMapper;
import com.erban.main.param.admin.PrettyErbanNoParam;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.admin.UserPrettyNoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 新版靓号管理
 */
@Service
public class PrettyNoAdminV2Service extends BaseService {
    @Autowired
    private UserDrawPrettyErbanNoMapper userDrawPrettyErbanNoMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AccountService accountService;

    public BusiResult getList(PrettyErbanNoParam prettyErbanNoParam) {
        if (prettyErbanNoParam.getBeginDate() != null) {
            prettyErbanNoParam.setBeginDate(prettyErbanNoParam.getBeginDate() + " 00:00:00");
        }
        if (prettyErbanNoParam.getEndDate() != null) {
            prettyErbanNoParam.setEndDate(prettyErbanNoParam.getEndDate() + " 23:59:59");
        }
        PageHelper.startPage(prettyErbanNoParam.getPage(), prettyErbanNoParam.getSize());
        List<UserPrettyNoVo> userPrettyNoVos = userDrawPrettyErbanNoMapper.selectByQuery(prettyErbanNoParam);
        if (userPrettyNoVos == null || userPrettyNoVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_PRETTYNO_NOTEXITLIST);
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(userPrettyNoVos));
    }

    /**
     * 新建靓号
     *
     * @param prettyErbanNo 靓号
     * @param erbanNo       原账号
     * @param seq           序号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult save(Long prettyErbanNo, Long erbanNo, Byte seq) {
        // 用户是否存在, 是否分配靓号
        Users users = usersService.getUsersByErBanNo(prettyErbanNo);
        if (users != null && users.getHasPrettyErbanNo()) {
            return new BusiResult(BusiStatus.ADMIN_PRETTYNO_EXIT);
        }

        // 靓号是否存在
        UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoMapper.selectByPrimaryKey(prettyErbanNo);
        if (userDrawPrettyErbanNo != null) {
            return new BusiResult(BusiStatus.ADMIN_PRETTYNO_EXIT);
        }

        if (erbanNo == null) {
            savePrettyNo(prettyErbanNo, erbanNo, (byte) 1, seq);
        } else {
            // 用户是否存在
            users = usersService.getUsersByErBanNo(erbanNo);
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }

            // 保存靓号
            savePrettyNo(prettyErbanNo, erbanNo, (byte) 2, seq);

            // 更新Users表
            updateUsersFromDBAndCache(prettyErbanNo, users);

            // 更新Account表
            Account account = accountService.getAccountByErBanNo(erbanNo);
            if (account != null) {
                updateAccountFromDB(prettyErbanNo, account);
            }

            // 协议更新
            jdbcTemplate.update("UPDATE `room_robot_group` SET `group_no`= ? WHERE `group_no`= ?", prettyErbanNo,
                    erbanNo);
            jdbcTemplate.update("UPDATE `room_robot_group_rela` SET `group_no`= ? WHERE `group_no`= ?", prettyErbanNo
                    , erbanNo);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult bind(Long prettyErbanNo, Long erbanNo) {
        // 靓号是否存在
        UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoMapper.selectByPrimaryKey(prettyErbanNo);
        if (userDrawPrettyErbanNo == null) {
            return new BusiResult(BusiStatus.ADMIN_PRETTYNO_NOTEXITLIST);
        }

        // 用户是否存在, 是否分配靓号
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        } else if (users.getHasPrettyErbanNo()) {
            return new BusiResult(BusiStatus.ADMIN_PRETTYNO_EXIT);
        }

        // 更新靓号
        updatePrettyNo(userDrawPrettyErbanNo, erbanNo);

        // 更新Users表
        updateUsersFromDBAndCache(prettyErbanNo, users);

        // 更新Account表
        Account account = accountService.getAccountByErBanNo(erbanNo);
        if (account != null) {
            updateAccountFromDB(prettyErbanNo, account);
        }

        // 协议更新
        jdbcTemplate.update("UPDATE `room_robot_group` SET `group_no`= ? WHERE `group_no`= ?", prettyErbanNo, erbanNo);
        jdbcTemplate.update("UPDATE `room_robot_group_rela` SET `group_no`= ? WHERE `group_no`= ?", prettyErbanNo,
                erbanNo);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 删除靓号并更新用户信息
     *
     * @param prettyErbanNo 靓号
     * @return
     */
    public BusiResult delete(Long prettyErbanNo) {
        UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoMapper.selectByPrimaryKey(prettyErbanNo);
        // 获取用户信息
        Users users = usersService.getUsersByErBanNo(prettyErbanNo);
        if (users != null && userDrawPrettyErbanNo.getUseErbanNo() != null) {
            // 删除靓号记录
            int result = userDrawPrettyErbanNoMapper.deleteByPrimaryKey(prettyErbanNo);
            if (result == 1) {
                // 更新Users表
                updateUsersToDBAndCache(userDrawPrettyErbanNo.getUseErbanNo(), users);
                // 更新Account表
                Account account = accountService.getAccountByErBanNo(prettyErbanNo);
                if (account != null) {
                    updateAccountFromDB(userDrawPrettyErbanNo.getUseErbanNo(), account);
                }
                return new BusiResult(BusiStatus.SUCCESS);
            }
        } else if (userDrawPrettyErbanNo.getUseErbanNo() == null) {
            // 删除靓号记录
            int result = userDrawPrettyErbanNoMapper.deleteByPrimaryKey(prettyErbanNo);
            if (result == 1) {
                return new BusiResult(BusiStatus.SUCCESS);
            }
        }
        return new BusiResult(BusiStatus.ADMIN_PRETTYNO_DELETE);
    }

    /**
     * 更新靓号以及状态
     *
     * @param userDrawPrettyErbanNo 靓号对象
     * @param erbanNo               原账号
     */
    public void updatePrettyNo(UserDrawPrettyErbanNo userDrawPrettyErbanNo, Long erbanNo) {
        userDrawPrettyErbanNo.setUseStatus((byte) 2);
        userDrawPrettyErbanNo.setUseErbanNo(erbanNo);
        userDrawPrettyErbanNoMapper.updateByPrimaryKeySelective(userDrawPrettyErbanNo);
    }

    /**
     * 保存靓号
     *
     * @param prettyErbanNo 靓号
     * @param erbanNo       原账号
     * @param status        状态
     * @param seq           序号
     */
    public void savePrettyNo(Long prettyErbanNo, Long erbanNo, byte status, Byte seq) {
        UserDrawPrettyErbanNo prettyNo = new UserDrawPrettyErbanNo();
        prettyNo.setCreateTime(new Date());
        prettyNo.setType((byte) 1);
        prettyNo.setPrettyErbanNo(prettyErbanNo);
        prettyNo.setUseErbanNo(erbanNo);
        prettyNo.setUseStatus(status);
        prettyNo.setSeq(seq);
        userDrawPrettyErbanNoMapper.insert(prettyNo);
    }

    /**
     * 更新用户信息以及缓存
     *
     * @param prettyErbanNo 拉贝号
     * @param users         用户信息
     */
    public void updateUsersFromDBAndCache(Long prettyErbanNo, Users users) {
        if (!CommonUtil.checkValidPhone(users.getPhone())) {
            users.setPhone(prettyErbanNo.toString());
        }
        users.setErbanNo(prettyErbanNo);
        users.setHasPrettyErbanNo(true);
        usersService.update(users);
        Gson gson = new Gson();
        String usersJson = gson.toJson(users);
        jedisService.hset(RedisKey.user.getKey(), users.getUid().toString(), usersJson);
    }

    /**
     * 更新用户信息以及缓存 (删除用)
     *
     * @param prettyErbanNo 拉贝号
     * @param users         用户信息
     */
    public void updateUsersToDBAndCache(Long prettyErbanNo, Users users) {
        if (!CommonUtil.checkValidPhone(users.getPhone())) {
            users.setPhone(prettyErbanNo.toString());
        }
        users.setErbanNo(prettyErbanNo);
        users.setHasPrettyErbanNo(false);
        usersService.update(users);
        Gson gson = new Gson();
        String usersJson = gson.toJson(users);
        jedisService.hset(RedisKey.user.getKey(), users.getUid().toString(), usersJson);
    }

    /**
     * 更新Account信息
     *
     * @param prettyErbanNo 拉贝号
     * @param account       账户信息
     */
    public void updateAccountFromDB(Long prettyErbanNo, Account account) {
        if (!CommonUtil.checkValidPhone(account.getPhone())) {
            account.setPhone(prettyErbanNo.toString());
        }
        account.setErbanNo(prettyErbanNo);
        accountService.update(account);
    }
}
