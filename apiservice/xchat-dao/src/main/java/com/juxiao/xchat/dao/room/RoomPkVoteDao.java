package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.RoomPkVoteDO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteHistroyDTO;
import com.juxiao.xchat.dao.room.query.PkVotesListQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 房间PK投票
 *
 * @class: RoomPkVoteMapper.java
 * @author: chenjunsheng
 * @date 2018/6/28
 */
public interface RoomPkVoteDao {

    /**
     * 保存数据库
     *
     * @param voteDo
     */
    @TargetDataSource
    void save(RoomPkVoteDO voteDo);

    /**
     * 更新数据库信息
     *
     * @param voteDo
     */
    @TargetDataSource
    void update(RoomPkVoteDO voteDo);

    /**
     * 删除PK记录
     *
     * @param voteId
     */
    @TargetDataSource
    @Delete("DELETE FROM `room_pk_vote`WHERE `id` = #{voteId}")
    void delete(@Param("voteId") Long voteId);

    /**
     * @param voteId
     * @param voteCount
     */
    @TargetDataSource
    @Update("UPDATE `room_pk_vote` SET `vote_count` = `vote_count` + #{voteCount},`update_time` = NOW() WHERE id = #{voteId}")
    void updateAddVote(@Param("voteId") Long voteId, @Param("voteCount") Integer voteCount);

    /**
     * @param voteId
     * @param pkVoteCount
     */
    @TargetDataSource
    @Update("UPDATE `room_pk_vote` SET `pk_vote_count` = `pk_vote_count` + #{pkVoteCount}, `update_time` = NOW() WHERE `id` = #{voteId}")
    void updateAddPkVote(@Param("voteId") Long voteId, @Param("pkVoteCount") Integer pkVoteCount);

    /**
     * 获取房间
     *
     * @param roomId
     * @return
     */
    @TargetDataSource(name = "ds2")
    RoomPkVoteDTO getEffectivePkVote(@Param("roomId") Long roomId);

    /**
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RoomPkVoteHistroyDTO> listPkVotes(PkVotesListQuery query);

    /**
     *
     * @param roomId
     * @return
     */
    @Select("SELECT COUNT(*) FROM `room_pk_vote` WHERE room_id = #{roomId} AND UNIX_TIMESTAMP(`create_time`) + expire_seconds + 15 > UNIX_TIMESTAMP(now())")
    int countIntervalPK(@Param("roomId") Long roomId);
}
