package com.erban.admin.main.service.system;


import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.mapper.AdminRefUserRoleMapper;
import com.erban.admin.main.mapper.AdminRoleMapper;
import com.erban.admin.main.mapper.AdminUserMapper;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.model.AdminUserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.EncrytcUtil;
import com.xchat.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


@Service("adminUserService")
public class AdminUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AdminRefUserRoleMapper adminRefUserRoleMapper;

    /**
     * 插入管理员信息
     *
     * @param adminUser
     * @return
     */
    public int insertAdminUser(AdminUser adminUser) {
        if (adminUser != null) {
            return adminUserMapper.insert(adminUser);
        }
        return 0;
    }

    /**
     * 判断管理员是否存在
     *
     * @param account
     * @param password
     * @return
     */
    public AdminUser getAdminUser(String account, String password) {
        AdminUserExample example = new AdminUserExample();
        logger.error("encodeMD5String=" + EncrytcUtil.encodeMD5String(password));
        example.createCriteria().andUsernameEqualTo(account).andPasswordEqualTo(EncrytcUtil.encodeMD5String(password)).andStatusEqualTo(1);
        List<AdminUser> list = adminUserMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public AdminUser getAdminUser(String account) {
        AdminUserExample example = new AdminUserExample();

        example.createCriteria().andPhoneEqualTo(account).andStatusEqualTo(1);
        List<AdminUser> list = adminUserMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public AdminUser getAdminUserById(int adminId) {
        return adminUserMapper.selectByPrimaryKey(adminId);
    }

    public int updateLastLogin(int adminId,String lastLoginIp) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setLastlogin(new Date());
        adminUser.setLastLoginIp(lastLoginIp);
        return adminUserMapper.updateByPrimaryKeySelective(adminUser);
    }

    /**
     * 插入管理员信息
     *
     * @param adminUser
     * @return
     */
    public int saveUser(AdminUser adminUser, boolean isEdit) {
        if (isEdit) {
            adminUserMapper.updateByPrimaryKeySelective(adminUser);
        } else {
            // 默认密码为123
            if (StringUtils.isBlank(adminUser.getUsername()) || StringUtils.isBlank(adminUser.getPassword())) {
                return ErrorCode.ERROR_NULL_ARGU;
            }
//            adminUser.setPassword(EncrytcUtil.encodeMD5String(adminUser.getPassword()));
            adminUser.setStatus(true);
            adminUser.setCreatetime(new Date());
            if (BlankUtil.isBlank(adminUser.getHeadimg())) {
                adminUser.setHeadimg("/static/AdminLTE/img/user2-160x160.jpg");
            }
            adminUserMapper.insert(adminUser);
        }
        return 1;
    }


    public PageInfo<AdminUser> getUserWithPage(String text, int page, int size) {
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(page, size);
//        AdminUserExample example = new AdminUserExample();
//        if (!BlankUtil.isBlank(text)) {
//            example.createCriteria().andUsernameLike("%" + text + "%");
//        }
//        List<AdminUser> list = adminUserMapper.selectByExample(example);
        List<AdminUser> list = adminUserMapper.selectAllUser(text);

        return new PageInfo<>(list);
    }

    public AdminUser getUserByName(String name) {
        if (BlankUtil.isBlank(name)) {
            return null;
        }
        AdminUserExample example = new AdminUserExample();
        example.createCriteria().andUsernameEqualTo(name);
        List<AdminUser> list = adminUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public int delUserById(Integer id) {
        return adminUserMapper.deleteByPrimaryKey(id);
    }
    public int updatePassword(Integer adminId,String password) {
    	logger.error("encodeMD5String=" + EncrytcUtil.encodeMD5String(password));
    	return adminUserMapper.updateByPassword(adminId, EncrytcUtil.encodeMD5String(password));
    }
}
