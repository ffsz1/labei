package com.erban.main.mybatismapper;

import com.erban.main.dto.MoraConfDTO;
import com.erban.main.model.MoraConfig;
import com.erban.main.model.MoraConfigExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MoraConfigMapper {
    int countByExample(MoraConfigExample example);

    int deleteByExample(MoraConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MoraConfig record);

    int insertSelective(MoraConfig record);

    List<MoraConfig> selectByExample(MoraConfigExample example);

    MoraConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MoraConfig record, @Param("example") MoraConfigExample example);

    int updateByExample(@Param("record") MoraConfig record, @Param("example") MoraConfigExample example);

    int updateByPrimaryKeySelective(MoraConfig record);

    int updateByPrimaryKey(MoraConfig record);

    List<MoraConfDTO> selectByPage(@Param("searchText") String searchText);

    MoraConfig selectByUid(@Param("uid") Long uid);
}
