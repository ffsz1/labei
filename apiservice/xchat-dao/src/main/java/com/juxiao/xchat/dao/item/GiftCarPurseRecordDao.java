package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.domain.GiftCarPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: GiftCarPurseRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface GiftCarPurseRecordDao {
    /**
     * 插入用户座驾记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void save(GiftCarPurseRecordDO recordDo);

    /**
     * 更新座驾记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void update(GiftCarPurseRecordDO recordDo);

    /**
     * 获取用户座驾
     *
     * @param uid
     * @param carId
     * @return
     */
    @TargetDataSource(name = "ds2")
    GiftCarPurseRecordDTO getUserCarPurseRecord(@Param("uid") Long uid, @Param("carId") Long carId);

    /**
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<GiftCarPurseRecordDTO> listUserInuseCars(@Param("uid") Long uid);

    /**
     * 查询用户座驾
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<Integer> listUserPurseCarids(@Param("uid") Long uid);
}