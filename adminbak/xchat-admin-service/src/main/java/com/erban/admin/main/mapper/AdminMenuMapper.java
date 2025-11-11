package com.erban.admin.main.mapper;

import com.erban.admin.main.model.AdminMenu;
import com.erban.admin.main.model.AdminMenuExample;
import java.util.List;
import java.util.Map;

import com.erban.admin.main.vo.RoleMenuVO;
import org.apache.ibatis.annotations.Param;

public interface AdminMenuMapper {
    int countByExample(AdminMenuExample example);

    int deleteByExample(AdminMenuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AdminMenu record);

    int insertSelective(AdminMenu record);

    List<AdminMenu> selectByExample(AdminMenuExample example);

    AdminMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AdminMenu record, @Param("example") AdminMenuExample example);

    int updateByExample(@Param("record") AdminMenu record, @Param("example") AdminMenuExample example);

    int updateByPrimaryKeySelective(AdminMenu record);

    int updateByPrimaryKey(AdminMenu record);

    List<RoleMenuVO> selectByParentList();

    List<AdminMenu> selectByMenuIds(Map<String, Object> params);

}
