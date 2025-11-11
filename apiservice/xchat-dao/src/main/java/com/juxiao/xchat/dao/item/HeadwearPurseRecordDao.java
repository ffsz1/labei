package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.domain.HeadwearPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;
import com.juxiao.xchat.dao.item.query.UserHeadwearRecordQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: HeadwearPurseRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface HeadwearPurseRecordDao {

    /**
     * @param recordDo
     */
    @TargetDataSource
    void save(HeadwearPurseRecordDO recordDo);

    /**
     * @param recordDo
     */
    @TargetDataSource
    void update(HeadwearPurseRecordDO recordDo);

    /**
     * 获取正在使用的头饰
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<HeadwearPurseRecordDTO> listInuseHeadwearRecord(@Param("uid") Long uid);

    /**
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    HeadwearPurseRecordDTO getUserHeadwearPurseRecord(UserHeadwearRecordQuery query);

    /**
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT record_id AS recordId,uid AS uid,headwear_id AS headwearId,headwear_date AS headwearDate,total_gold_num AS totalGoldNum,is_use AS isUse,create_time AS createTime " +
            "FROM `headwear_purse_record` " +
            "WHERE uid = #{uid} " +
            "AND headwear_date > 0 order by create_time desc")
    List<HeadwearPurseRecordDTO> listUserHeadwearRecord(@Param("uid") Long uid);
}