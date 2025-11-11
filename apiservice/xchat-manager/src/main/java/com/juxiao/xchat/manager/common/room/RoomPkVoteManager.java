package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.dao.room.dto.RoomPkVoteDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;

import java.util.Set;

/**
 * 房间投票
 *
 * @class: RoomPkVoteManager.java
 * @author: chenjunsheng
 * @date 2018/7/3
 */
public interface RoomPkVoteManager {

    /**
     * 刷礼物进行投票
     *
     * @param roomUid
     * @param recvUid
     * @param goldNum
     */
    void goldVote(Long roomUid, Long recvUid, Long goldNum);

    /**
     * 根据房间获取PK投票
     *
     * @param roomId
     * @return
     */
    RoomPkVoteDTO getRoomPkVote(Long roomId);

    /**
     * 清理PK投票缓存
     *
     * @param voteDto
     */
    void clearRoomVote(RoomPkVoteDTO voteDto);

    /**
     * 获取投票用户集合
     *
     * @param roomId
     * @param uid
     * @return
     */
    Set<Long> getRoomVoteSet(Long roomId, Long uid);

    /**
     * 组装单个投票返回结果
     *
     * @param voteDto
     * @return
     */
    RoomPkVoteResultDTO getPkVoteResult(RoomPkVoteDTO voteDto);
}
