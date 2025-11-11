package com.erban.admin.main.mapper;

import com.erban.admin.main.model.TestPurse;
import com.erban.admin.main.model.TestPurseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestPurseMapper {
    int countByExample(TestPurseExample example);

    int deleteByExample(TestPurseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestPurse record);

    int insertSelective(TestPurse record);

    List<TestPurse> selectByExample(TestPurseExample example);

    TestPurse selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestPurse record, @Param("example") TestPurseExample example);

    int updateByExample(@Param("record") TestPurse record, @Param("example") TestPurseExample example);

    int updateByPrimaryKeySelective(TestPurse record);

    int updateByPrimaryKey(TestPurse record);
}
