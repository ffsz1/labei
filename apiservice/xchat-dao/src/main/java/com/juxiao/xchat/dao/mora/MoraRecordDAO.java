package com.juxiao.xchat.dao.mora;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mora.domain.MoraRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-01
 * @time 22:52
 */
public interface MoraRecordDAO {

    /**
     * 保存
     * @param recordDO recordDO
     * @return int
     */
    @TargetDataSource
    @Insert("insert into mora_record(id, room_id, uid, room_uid, type, probability, total, gift_id, num, choose, is_valid, ref_id, is_return_gold, is_finish, expiration_date, result, create_time) values (#{id},#{roomId},#{uid},#{roomUid},#{type},#{probability},#{total},#{giftId},#{num},#{choose},#{isValid},#{refId},#{isReturnGold},#{isFinish},#{expirationDate},#{result},#{createTime, jdbcType=TIMESTAMP})")
    int save(MoraRecordDO recordDO);


    /**
     * 根据recordId查询
     * @param recordId recordId
     * @return MoraRecordDO
     */
    @TargetDataSource(name = "ds2")
    @Select("select id, room_id as roomId, uid, room_uid as roomUid, type, probability, total, gift_id as giftId, num, choose, is_valid as isValid, ref_id as refId, is_return_gold as isReturnGold, is_finish as isFinish, expiration_date as expirationDate, result, create_time as createTime from mora_record where id = #{recordId}")
    MoraRecordDO selectByMoraRecordId(Integer recordId);

    /**
     *  更新
     * @param moraRecordDO moraRecordDO
     * @return int
     */
    @TargetDataSource
    void updateById(MoraRecordDO moraRecordDO);

    /**
     *  根据refId更新
     * @param recordDO recordDO
     */
    @TargetDataSource
    void updateByRefId(MoraRecordDO recordDO);


    /**
     * 查询分页数据
     * @param uid uid
     * @param current 当前页
     * @param pageSize 每页显示数
     * @return List<MoraRecordDO>
     */
    @TargetDataSource(name = "ds2")
    @Select("select id, room_id as roomId, uid, room_uid as roomUid, type, probability, total, gift_id as giftId, num, choose, is_valid as isValid, ref_id as refId, is_return_gold as isReturnGold, is_finish as isFinish, expiration_date as expirationDate, result, create_time as createTime from mora_record where result is not null and uid = #{uid} order by create_time desc limit #{current} , #{pageSize}")
    List<MoraRecordDO> selectByPage(Long uid,Integer current, Integer pageSize);

    /**
     * 获取未结束发起的记录
     * @return List<MoraRecordDO>
     */
    @TargetDataSource(name = "ds2")
    @Select("select id, room_id as roomId, uid, room_uid as roomUid, type, probability, total, gift_id as giftId, num, choose, is_valid as isValid, ref_id as refId, is_return_gold as isReturnGold, is_finish as isFinish, expiration_date as expirationDate, result, create_time as createTime from mora_record where is_finish = 2 and type = 1 and is_valid = 1")
    List<MoraRecordDO> selectByValidMoraRecord();

    /**
     * 根据房间ID获取未参与的发起记录
     * @param roomId roomId
     * @return List
     */
    @TargetDataSource(name = "ds2")
    @Select("select id, room_id as roomId, uid, room_uid as roomUid, type, probability, total, gift_id as giftId, num, choose, is_valid as isValid, ref_id as refId, is_return_gold as isReturnGold, is_finish as isFinish, expiration_date as expirationDate, result, create_time as createTime from mora_record where is_finish = 2 and type = 1 and is_valid = 1 and room_id = #{roomId}")
    List<MoraRecordDO> selectByRoomId(Long roomId);
}
