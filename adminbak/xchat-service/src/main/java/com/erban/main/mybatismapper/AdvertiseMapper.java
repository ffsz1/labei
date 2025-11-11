package com.erban.main.mybatismapper;

import com.erban.main.base.BaseMapper;
import com.erban.main.model.Advertise;
import com.erban.main.model.AdvertiseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdvertiseMapper extends BaseMapper {
    int countByExample(AdvertiseExample example);

    int deleteByExample(AdvertiseExample example);

    int deleteByPrimaryKey(Integer advId);

    int insert(Advertise record);

    int insertSelective(Advertise record);

    List<Advertise> selectByExample(AdvertiseExample example);

    Advertise selectByPrimaryKey(Integer advId);

    int updateByExampleSelective(@Param("record") Advertise record, @Param("example") AdvertiseExample example);

    int updateByExample(@Param("record") Advertise record, @Param("example") AdvertiseExample example);

    int updateByPrimaryKeySelective(Advertise record);

    int updateByPrimaryKey(Advertise record);
}
