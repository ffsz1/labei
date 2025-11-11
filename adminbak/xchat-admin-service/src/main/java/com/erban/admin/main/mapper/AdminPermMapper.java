package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.AdminMenuVo;

import java.util.List;

public interface AdminPermMapper {
    List<AdminMenuVo> selectAllMenuByAdminId(Integer adminId);
}
