package com.erban.admin.main.vo;

import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.vo
 * @date 2018/8/13
 * @time 15:34
 */
public class RoleMenuVO {


    private Integer menuId;

    private String menuName;

    private String pName;

    private Boolean check;

    private Integer parentId;


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
