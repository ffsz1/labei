package com.erban.main.mybatismapper;

import com.erban.main.model.NobleZip;
import com.erban.main.model.NobleZipExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleZipMapper {
    int countByExample(NobleZipExample example);

    int deleteByExample(NobleZipExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NobleZip record);

    int insertSelective(NobleZip record);

    List<NobleZip> selectByExampleWithBLOBs(NobleZipExample example);

    List<NobleZip> selectByExample(NobleZipExample example);

    NobleZip selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NobleZip record, @Param("example") NobleZipExample example);

    int updateByExampleWithBLOBs(@Param("record") NobleZip record, @Param("example") NobleZipExample example);

    int updateByExample(@Param("record") NobleZip record, @Param("example") NobleZipExample example);

    int updateByPrimaryKeySelective(NobleZip record);

    int updateByPrimaryKeyWithBLOBs(NobleZip record);

    int updateByPrimaryKey(NobleZip record);
}
