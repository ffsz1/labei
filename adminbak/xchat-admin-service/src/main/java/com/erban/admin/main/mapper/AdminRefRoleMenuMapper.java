package com.erban.admin.main.mapper;

import com.erban.admin.main.model.AdminRefRoleMenuExample;
import com.erban.admin.main.model.AdminRefRoleMenuKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRefRoleMenuMapper {
    int countByExample(AdminRefRoleMenuExample example);

    int deleteByExample(AdminRefRoleMenuExample example);

    int deleteByPrimaryKey(AdminRefRoleMenuKey key);

    int insert(AdminRefRoleMenuKey record);

    int insertSelective(AdminRefRoleMenuKey record);

    List<AdminRefRoleMenuKey> selectByExample(AdminRefRoleMenuExample example);

    int updateByExampleSelective(@Param("record") AdminRefRoleMenuKey record, @Param("example") AdminRefRoleMenuExample example);

    int updateByExample(@Param("record") AdminRefRoleMenuKey record, @Param("example") AdminRefRoleMenuExample example);


    List<AdminRefRoleMenuKey> selectRoleIdByList(@Param("roleId") Integer roleId);

    int insertBatch(List<AdminRefRoleMenuKey> adminRefRoleMenuKeys);
}
