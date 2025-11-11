package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteHistroyDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;
import com.juxiao.xchat.service.api.room.bo.RoomPkVoteSaveBO;

import java.util.List;

/**
 * 房间内部PK接口
 *
 * @class: RoomPkVoteService.java
 * @author: chenjunsheng
 * @date 2018/7/3
 */
public interface RoomPkVoteService {

    /**
     * 发起并保存一个PK投票
     *
     * @param saveBo
     */
    Long save(RoomPkVoteSaveBO saveBo) throws WebServiceException;

    /**
     * 取消
     *
     * @param roomId
     * @param uid
     */
    void cancel(Long roomId, Long uid) throws WebServiceException;

    /**
     * 用户投票模式
     *
     * @param roomId
     * @param uid
     * @param voteUid
     * @return
     */
    RoomPkVoteResultDTO userVote(Long roomId, Long uid, Long voteUid) throws WebServiceException;

    /**
     * 获取最新的投票结果
     *
     * @param roomId
     * @return
     */
    RoomPkVoteResultDTO getPkVote(Long roomId) throws WebServiceException;

    /**
     * 获取房间内PK历史列表
     *
     * @param roomId
     * @param pageNum
     * @param pageSisz
     * @return
     */
    List<RoomPkVoteHistroyDTO> listPkVotes(Long roomId, String os, Integer pageNum, Integer pageSisz);

    /**
     * 定时任务定时检查
     *
     * @return
     */
    int checkFinish();
}
