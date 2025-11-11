package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.dto.UserDrawRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: UserDrawRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface UserDrawRecordDao {
    
    @TargetDataSource
    void save(UserDrawRecordDO recordDo);

    /**
     * 更新用户抽检记录
     *
     * @param recordDo
     */
    @TargetDataSource
    void update(UserDrawRecordDO recordDo);

    /**
     * 获取用户未使用的抽奖记录
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserDrawRecordDTO getUserCreateDrawRecord(@Param("uid") Long uid, @Param("types") List<Byte> types);

    /**
     * 获取用户未使用的抽奖记录
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserDrawRecordDTO getUserCreateDrawRecord2(@Param("uid") Long uid, @Param("type") Byte type);


    /**
     * 获取用户未使用的抽奖记录
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserDrawRecordDTO getUserCreateDrawRecord3(@Param("uid") Long uid, @Param("type") Byte type, @Param("srcObjId")String srcObjId);

    /**
     * 获取已经中奖的记录，用于滚屏
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<UserDrawRecordDTO> listUserDrawWinRecord();
}
