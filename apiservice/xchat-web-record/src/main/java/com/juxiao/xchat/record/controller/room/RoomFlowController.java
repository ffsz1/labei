package com.juxiao.xchat.record.controller.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.RoomDrawRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDetailDTO;
import com.juxiao.xchat.service.record.room.RoomFlowServie;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 房间流水统计
 */
@RestController
@RequestMapping("/stat/roomFlow")
public class RoomFlowController {


    @Autowired
    private RoomFlowServie roomFlowServie;

    /**
     * 获取某个房间的房间流水
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getOne", method = RequestMethod.GET)
    public WebServiceMessage getRoomFlow(@Param("roomUid") String roomUid) throws Exception {
        List<RoomFlowRecordDTO> list = roomFlowServie.listRoomFlowRecord(roomUid);
        return WebServiceMessage.success(list);
    }

    /**
     * 获取某个房间的房间流水明细
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getDetail", method = RequestMethod.GET)
    public WebServiceMessage getRoomFlowDetail(String roomUid, String date) throws Exception {
        List<RoomFlowRecordDetailDTO> list = roomFlowServie.listRoomFlowDetail(roomUid, date);
        return WebServiceMessage.success(list);
    }

    /**
     * 获取捡海螺房间的列表
     *
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "getRoomUidByList", method = RequestMethod.GET)
    public WebServiceMessage listRoomDraw(@RequestParam("roomUid") String roomUid) throws WebServiceException {
        List<RoomDrawRecordDTO> list = roomFlowServie.listRoomDraw(roomUid);
        return WebServiceMessage.success(list);
    }
}
