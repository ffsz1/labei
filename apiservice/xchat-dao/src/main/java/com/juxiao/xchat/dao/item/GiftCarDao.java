package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 座驾数据库操作接口
 *
 * @class: GiftCarDTO.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface GiftCarDao {

    /**
     * 查询数据库中的座驾
     *
     * @param carId
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    @TargetDataSource(name = "ds2")
    GiftCarDTO getGiftCar(@Param("carId") Integer carId);

    /**
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT car_id FROM gift_car WHERE car_status = 1 AND allow_purse = 1 ORDER BY seq_no ASC")
    List<String> listAllCarids();

    /**
     * 查询商城座驾
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT car_id FROM gift_car WHERE car_status = 1 AND allow_purse = 1 ORDER BY create_time desc,gold_price asc")
    List<String> listByMall();
}