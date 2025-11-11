package com.erban.main.mybatismapper;

import com.erban.main.model.FaceJson;
import com.erban.main.model.FaceJsonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FaceJsonMapper {
    int countByExample(FaceJsonExample example);

    int deleteByExample(FaceJsonExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FaceJson record);

    int insertSelective(FaceJson record);

    List<FaceJson> selectByExampleWithBLOBs(FaceJsonExample example);

    List<FaceJson> selectByExample(FaceJsonExample example);

    FaceJson selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FaceJson record, @Param("example") FaceJsonExample example);

    int updateByExampleWithBLOBs(@Param("record") FaceJson record, @Param("example") FaceJsonExample example);

    int updateByExample(@Param("record") FaceJson record, @Param("example") FaceJsonExample example);

    int updateByPrimaryKeySelective(FaceJson record);

    int updateByPrimaryKeyWithBLOBs(FaceJson record);

    int updateByPrimaryKey(FaceJson record);

    FaceJson getFaceJson();
}
