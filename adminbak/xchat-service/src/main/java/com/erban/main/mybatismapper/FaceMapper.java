package com.erban.main.mybatismapper;

import com.erban.main.model.Face;
import com.erban.main.model.FaceExample;
import java.util.List;

public interface FaceMapper {
    int deleteByPrimaryKey(Integer faceId);

    int insert(Face record);

    int insertSelective(Face record);

    List<Face> selectByExample(FaceExample example);

    Face selectByPrimaryKey(Integer faceId);

    int updateByPrimaryKeySelective(Face record);

    int updateByPrimaryKey(Face record);
}
