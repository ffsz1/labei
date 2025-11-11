package com.erban.main.mybatismapper;

import com.erban.main.base.BaseMapper;
import com.erban.main.model.GiftCar;
import com.erban.main.model.GiftCarExample;
import java.util.List;

import com.erban.main.model.GiftCarGetRecord;
import org.apache.ibatis.annotations.Param;

public interface GiftCarMapper extends BaseMapper<GiftCar, GiftCarExample> {
    int countByExample(GiftCarExample example);

    int deleteByExample(GiftCarExample example);

    int deleteByPrimaryKey(Integer carId);

    int insert(GiftCar record);

    int insertSelective(GiftCar record);

    List<GiftCar> selectByExample(GiftCarExample example);

    GiftCar selectByPrimaryKey(Integer carId);

    int updateByExampleSelective(@Param("record") GiftCar record, @Param("example") GiftCarExample example);

    int updateByExample(@Param("record") GiftCar record, @Param("example") GiftCarExample example);

    int updateByPrimaryKeySelective(GiftCar record);

    int updateByPrimaryKey(GiftCar record);
}
