package com.erban.admin.main.vo;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.vo
 * @date 2018/8/13
 * @time 19:13
 */
public class MenuVO {

    private List<Integer> menus;

    private Integer roleId;


    public List<Integer> getMenus() {
        return menus;
    }

    public void setMenus(List<Integer> menus) {
        this.menus = menus;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
