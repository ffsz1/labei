package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteHistroyDTO;
import com.juxiao.xchat.dao.room.dto.RoomPkVoteResultDTO;
import com.juxiao.xchat.service.api.room.RoomPkVoteService;
import com.juxiao.xchat.service.api.room.bo.RoomPkVoteSaveBO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/room/pkvote")
@Api(description = "房间接口", tags = "房间接口")
public class RoomPkVoteController {
    @Autowired
    private RoomPkVoteService pkvoteService;

    /**
     * 保存发起一个投票
     *
     * @param saveBo
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public WebServiceMessage save(RoomPkVoteSaveBO saveBo) throws WebServiceException {
        Long voteId = pkvoteService.save(saveBo);
        if (voteId != null) {
            return WebServiceMessage.success(voteId);
        }
        return WebServiceMessage.failure(WebServiceCode.ERROR3);
    }

    /**
     * @param roomId
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public WebServiceMessage cancel(@RequestParam("roomId") Long roomId, @RequestParam("uid") Long uid) throws WebServiceException {
        pkvoteService.cancel(roomId, uid);
        return WebServiceMessage.success(null);
    }

    /**
     * 投票接口
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public WebServiceMessage vote(@RequestParam("roomId") Long roomId,
                                  @RequestParam(value = "uid", required = false) Long uid,
                                  @RequestParam(value = "voteUid", required = false) Long voteUid) throws WebServiceException {
        RoomPkVoteResultDTO resultDto = pkvoteService.userVote(roomId, uid, voteUid);
        return WebServiceMessage.success(resultDto);
    }

    /**
     * 获取当前投票相关信息
     *
     * @param roomId
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public WebServiceMessage getPkVote(@RequestParam("roomId") Long roomId) throws WebServiceException {
        RoomPkVoteResultDTO resultDto = pkvoteService.getPkVote(roomId);
        return WebServiceMessage.success(resultDto);
    }

    /**
     * @param roomId
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public WebServiceMessage listPkVotes(@RequestParam("roomId") Long roomId,
                                         @RequestParam(value = "os", required = false) String os,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(value = "pageSisz", required = false, defaultValue = "20") Integer pageSisz) {
        List<RoomPkVoteHistroyDTO> list = pkvoteService.listPkVotes(roomId, os, pageNum, pageSize == null ? pageSisz : pageSize);
        return WebServiceMessage.success(list);
    }
}
