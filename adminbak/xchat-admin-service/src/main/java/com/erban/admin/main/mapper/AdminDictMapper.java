package com.erban.admin.main.mapper;

import com.erban.admin.main.model.AdminDict;
import com.erban.admin.main.model.AdminDictExample;
import com.erban.admin.main.model.AdminDictKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminDictMapper {
    int countByExample(AdminDictExample example);

    int deleteByExample(AdminDictExample example);

    int deleteByPrimaryKey(AdminDictKey key);

    int insert(AdminDict record);

    int insertSelective(AdminDict record);

    List<AdminDict> selectByExample(AdminDictExample example);

    AdminDict selectByPrimaryKey(AdminDictKey key);

    int updateByExampleSelective(@Param("record") AdminDict record, @Param("example") AdminDictExample example);

    int updateByExample(@Param("record") AdminDict record, @Param("example") AdminDictExample example);

    int updateByPrimaryKeySelective(AdminDict record);

    int updateByPrimaryKey(AdminDict record);
}
