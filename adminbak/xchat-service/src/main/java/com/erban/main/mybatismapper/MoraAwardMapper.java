package com.erban.main.mybatismapper;

import com.erban.main.dto.MoraAwardDTO;
import com.erban.main.dto.MoraConfDTO;
import com.erban.main.model.MoraAward;
import com.erban.main.model.MoraAwardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MoraAwardMapper {
    int countByExample(MoraAwardExample example);

    int deleteByExample(MoraAwardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MoraAward record);

    int insertSelective(MoraAward record);

    List<MoraAward> selectByExample(MoraAwardExample example);

    MoraAward selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MoraAward record, @Param("example") MoraAwardExample example);

    int updateByExample(@Param("record") MoraAward record, @Param("example") MoraAwardExample example);

    int updateByPrimaryKeySelective(MoraAward record);

    int updateByPrimaryKey(MoraAward record);

    int selectByIsUse(@Param("probability") Integer probability);

    List<MoraAwardDTO> selectByPage(@Param("searchText") String searchText);
}
