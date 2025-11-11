package com.erban.admin.main.service.system;


import com.erban.admin.main.mapper.AdminMenuMapper;
import com.erban.admin.main.mapper.AdminPermMapper;
import com.erban.admin.main.mapper.AdminRefRoleMenuMapper;
import com.erban.admin.main.model.AdminMenu;
import com.erban.admin.main.model.AdminMenuExample;
import com.erban.admin.main.model.AdminRefRoleMenuExample;
import com.erban.admin.main.vo.AdminMenuVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 菜单服务类
 */
@Service("adminMenuService")
public class AdminMenuService {
    @Autowired
    private AdminMenuMapper adminMenuMapper;

    @Autowired
    private AdminPermMapper adminPermMapper;

    @Autowired
    private AdminRefRoleMenuMapper adminRefRoleMenuMapper;

    /**
     * 查询管理员拥有的菜单
     *
     * @param adminId
     * @return
     */
    public List<AdminMenuVo> getAllMenu(int adminId) {
        return adminPermMapper.selectAllMenuByAdminId(adminId);
    }

    /**
     * 分页查询菜单
     *
     * @param name 菜单名称
     * @param page
     * @param size
     * @return
     */
    public PageInfo<AdminMenuVo> getMenuByPage(String name, int page, int size) {
        // 获取所有父级目录信息
        AdminMenuExample example1 = new AdminMenuExample();
        example1.createCriteria().andParentidEqualTo(0);
        List<AdminMenu> plist = adminMenuMapper.selectByExample(example1);

        AdminMenuExample example = new AdminMenuExample();
        if (!BlankUtil.isBlank(name)) {
            example.createCriteria().andNameLike("%"+name+"%");
        }
        PageHelper.startPage(page, size);
        List<AdminMenu> list = adminMenuMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(transToMenuVo(list,plist));
        return pageInfo;
    }

    /**
     * 获取父级菜单
     * @return
     */
    public List<AdminMenu> getParentMenus() {
        AdminMenuExample example = new AdminMenuExample();
        example.createCriteria().andParentidEqualTo(0);
        return adminMenuMapper.selectByExample(example);
    }

    public int saveMenu(AdminMenu adminMenu, boolean isEdit) {
        adminMenu.setCreatetime(new Date());
        if (isEdit) {
            return adminMenuMapper.updateByPrimaryKey(adminMenu);
        } else {
            return adminMenuMapper.insert(adminMenu);
        }
    }

    public AdminMenu getAdminMenuById(int id) {
        return adminMenuMapper.selectByPrimaryKey(id);
    }

    public void delMenuById(Integer id) {
        adminMenuMapper.deleteByPrimaryKey(id);
    }

    /**
     *
     * @param list
     * @param plist
     * @return
     */
    public List<AdminMenuVo> transToMenuVo(List<AdminMenu> list, List<AdminMenu> plist) {
        List<AdminMenuVo> volist = Lists.newArrayList();
        for (AdminMenu adminMenu : list) {
            AdminMenuVo adminMenuVo = new AdminMenuVo();
            adminMenuVo.setCreatetime(adminMenu.getCreatetime());
            adminMenuVo.setDescription(adminMenu.getDescription());
            adminMenuVo.setIcon(adminMenu.getIcon());
            adminMenuVo.setId(adminMenu.getId());
            adminMenuVo.setParentid(adminMenu.getParentid());
            adminMenuVo.setPath(adminMenu.getPath());
            adminMenuVo.setName(adminMenu.getName());
            adminMenuVo.setShoworder(adminMenu.getShoworder());
            adminMenuVo.setStatus(adminMenu.getStatus());
            for (AdminMenu pmenu : plist) {
                if (adminMenu.getParentid().equals(pmenu.getId())) {
                    adminMenuVo.setParentstr(pmenu.getName());
                }
            }
            volist.add(adminMenuVo);
        }
        return volist;
    }

    public int delRoleMenuByRoleId(Integer roleId) {
        AdminRefRoleMenuExample adminRefRoleMenuExample  = new AdminRefRoleMenuExample();
        adminRefRoleMenuExample.createCriteria().andRoleIdEqualTo(roleId);
        return adminRefRoleMenuMapper.deleteByExample(adminRefRoleMenuExample);
    }
}
