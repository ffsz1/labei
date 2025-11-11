package com.erban.admin.main.service.system;

import com.erban.admin.main.mapper.AdminMenuMapper;
import com.erban.admin.main.mapper.AdminRefRoleMenuMapper;
import com.erban.admin.main.mapper.AdminRefUserRoleMapper;
import com.erban.admin.main.mapper.AdminRoleMapper;
import com.erban.admin.main.model.*;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.MenuVO;
import com.erban.admin.main.vo.RoleMenuVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminRoleService extends BaseService{

    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AdminRefUserRoleMapper adminRefUserRoleMapper;
    @Autowired
    private AdminRefRoleMenuMapper adminRefRoleMenuMapper;

    @Autowired
    private AdminMenuMapper adminMenuMapper;

    /**
     * 插入管理员信息
     * @param adminRole
     * @return
     */
    public int saveRole(AdminRole adminRole, boolean isEdit){
        if(isEdit){
            adminRoleMapper.updateByPrimaryKey(adminRole);
        }else{
            adminRoleMapper.insert(adminRole);
        }
        return 1;
    }


    public AdminRole getRoleById(Integer roleId){
        return adminRoleMapper.selectByPrimaryKey(roleId);
    }


    public PageInfo<AdminRole> getRoleWithPage(String name, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        PageHelper.startPage(page, size);
        AdminRoleExample example = new AdminRoleExample();
        if(name != null && !name.equals("")){
            example.createCriteria().andNameLike("%"+name+"%");
        }
        List<AdminRole> list = adminRoleMapper.selectByExample(example);
        return new PageInfo<>(list);
    }


    public int delRoleById(Integer id){
        return adminRoleMapper.deleteByPrimaryKey(id);
    }

    public List<AdminRole> getAllRole(){
        return adminRoleMapper.selectByExample(new AdminRoleExample());
    }

    public List<AdminRefUserRoleKey> getRoleByAdminId(Integer adminId){
        AdminRefUserRoleExample example = new AdminRefUserRoleExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        return adminRefUserRoleMapper.selectByExample(example);
    }

    public int saveUserRole(Integer adminId , Integer[] roles){
        AdminRefUserRoleExample example = new AdminRefUserRoleExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        int result = adminRefUserRoleMapper.deleteByExample(example);
        if (roles == null) {
            return 1;
        }
        for(Integer roleId: roles){
            AdminRefUserRoleKey adminRefUserRole = new AdminRefUserRoleKey();
            adminRefUserRole.setAdminId(adminId);
            adminRefUserRole.setRoleId(roleId);
            adminRefUserRoleMapper.insert(adminRefUserRole);
        }
        return 1;
    }


    public List<AdminRefRoleMenuKey> getMenuByRole(Integer roleId){
        AdminRefRoleMenuExample example = new AdminRefRoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return adminRefRoleMenuMapper.selectByExample(example);
    }

    public int saveRoleMenu(Integer roleId , Integer[] menus){
        AdminRefRoleMenuExample example = new AdminRefRoleMenuExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        int result = adminRefRoleMenuMapper.deleteByExample(example);
        if (menus == null) {
            return 1;
        }
        for(Integer menuId: menus){
            AdminRefRoleMenuKey adminRefRoleMenu = new AdminRefRoleMenuKey();
            adminRefRoleMenu.setMenuId(menuId);
            adminRefRoleMenu.setRoleId(roleId);
            adminRefRoleMenuMapper.insert(adminRefRoleMenu);
        }
        return 1;
    }

    public List<RoleMenuVO> queryByRoleMenuLisy(Integer roleId){
        List<AdminRefRoleMenuKey> adminRefRoleMenuKeys = adminRefRoleMenuMapper.selectRoleIdByList(roleId);
        List<RoleMenuVO> roleMenuVOS = this.adminMenuMapper.selectByParentList();
        if(adminRefRoleMenuKeys != null && adminRefRoleMenuKeys.size() > 0){
            Set<Integer> menuSet = adminRefRoleMenuKeys.stream().map(AdminRefRoleMenuKey::getMenuId).collect(Collectors.toSet());
            roleMenuVOS.forEach(item ->{
                item.setCheck(menuSet.contains(item.getMenuId()));
            });
        }else{
            roleMenuVOS.forEach(item -> {
                item.setCheck(false);
            });
        }

        return roleMenuVOS;
    }

    public int insertRoleMenu(MenuVO menuVO) {
        Map<String,Object> params = new HashMap<>(1);
        params.put("menuIds",menuVO.getMenus());
        List<AdminMenu> adminMenus = this.adminMenuMapper.selectByMenuIds(params);
        AdminRefRoleMenuKey adminRefRoleMenuKey = new AdminRefRoleMenuKey();
        List<Integer> menuIdList = menuVO.getMenus();
        Set<Integer> menuSet = adminMenus.stream().map(AdminMenu::getId).collect(Collectors.toSet());
        List<AdminRefRoleMenuKey> adminRefRoleMenuKeys = new ArrayList<>();
        menuIdList.forEach(item -> {
            menuSet.add(item);
        });
        menuSet.stream().forEach(item ->{
            AdminRefRoleMenuKey adminRefRoleMenu = new AdminRefRoleMenuKey();
            adminRefRoleMenu.setMenuId(item);
            adminRefRoleMenu.setRoleId(menuVO.getRoleId());
            adminRefRoleMenuKeys.add(adminRefRoleMenu);
        });
        int result = this.adminRefRoleMenuMapper.insertBatch(adminRefRoleMenuKeys);
        return result;
    }
}
