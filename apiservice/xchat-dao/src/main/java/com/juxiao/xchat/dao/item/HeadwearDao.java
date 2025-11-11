package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: HeadwearDao.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface HeadwearDao {

    /**
     * @param headwearId
     * @return
     */
    @TargetDataSource(name = "ds2")
    HeadwearDTO getHeadwear(@Param("headwearId") Integer headwearId);

    /**
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT headwear_id FROM headwear WHERE headwear_status = 1 ORDER BY seq_no ASC")
    List<String> listAllHeadwearids();

    /**
     * 查询商城头饰
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT headwear_id FROM headwear WHERE headwear_status = 1 AND allow_purse = 1 ORDER BY  create_time desc,gold_price asc")
    List<String> listByMall();
}