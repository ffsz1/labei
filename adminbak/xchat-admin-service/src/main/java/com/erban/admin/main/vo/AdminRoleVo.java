package com.erban.admin.main.vo;

import com.erban.admin.main.model.AdminRole;

public class AdminRoleVo extends AdminRole {
    private boolean isRole;

    public boolean isRole() {
        return isRole;
    }

    public void setRole(boolean role) {
        isRole = role;
    }
}
