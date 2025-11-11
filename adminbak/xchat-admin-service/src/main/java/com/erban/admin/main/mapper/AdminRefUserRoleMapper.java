package com.erban.admin.main.mapper;

import com.erban.admin.main.model.AdminRefUserRoleExample;
import com.erban.admin.main.model.AdminRefUserRoleKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRefUserRoleMapper {
    int countByExample(AdminRefUserRoleExample example);

    int deleteByExample(AdminRefUserRoleExample example);

    int deleteByPrimaryKey(AdminRefUserRoleKey key);

    int insert(AdminRefUserRoleKey record);

    int insertSelective(AdminRefUserRoleKey record);

    List<AdminRefUserRoleKey> selectByExample(AdminRefUserRoleExample example);

    int updateByExampleSelective(@Param("record") AdminRefUserRoleKey record, @Param("example") AdminRefUserRoleExample example);

    int updateByExample(@Param("record") AdminRefUserRoleKey record, @Param("example") AdminRefUserRoleExample example);
}
