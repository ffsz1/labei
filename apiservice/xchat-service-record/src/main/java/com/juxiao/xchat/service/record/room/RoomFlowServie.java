package com.juxiao.xchat.service.record.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDTO;
import com.juxiao.xchat.dao.item.dto.RoomFlowRecordDetailDTO;
import com.juxiao.xchat.dao.item.dto.RoomDrawRecordDTO;

import java.util.List;

public interface RoomFlowServie {

    /**
     * 查询房间流水
     *
     * @param roomUid
     * @return
     */
    List<RoomFlowRecordDTO> listRoomFlowRecord(String roomUid) throws Exception;

    /**
     * 查询房间流水详情
     *
     * @param roomUid
     * @param date
     * @return
     */
    List<RoomFlowRecordDetailDTO> listRoomFlowDetail(String roomUid, String date) throws Exception;

    /**
     * 查看房间捡海螺流水
     *
     * @param roomUidStr
     * @return
     */
    List<RoomDrawRecordDTO> listRoomDraw(String roomUidStr) throws WebServiceException;
}
