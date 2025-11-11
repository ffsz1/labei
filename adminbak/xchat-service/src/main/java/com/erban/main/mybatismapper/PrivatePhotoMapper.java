package com.erban.main.mybatismapper;

import com.erban.main.model.PrivatePhoto;
import com.erban.main.model.PrivatePhotoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivatePhotoMapper {
    int countByExample(PrivatePhotoExample example);

    int deleteByExample(PrivatePhotoExample example);

    int deleteByPrimaryKey(Long pid);

    int insert(PrivatePhoto record);

    int insertSelective(PrivatePhoto record);

    List<PrivatePhoto> selectByExample(PrivatePhotoExample example);

    PrivatePhoto selectByPrimaryKey(Long pid);

    int updateByExampleSelective(@Param("record") PrivatePhoto record, @Param("example") PrivatePhotoExample example);

    int updateByExample(@Param("record") PrivatePhoto record, @Param("example") PrivatePhotoExample example);

    int updateByPrimaryKeySelective(PrivatePhoto record);

    int updateByPrimaryKey(PrivatePhoto record);
}
