package com.erban.admin.web.task;

import com.erban.admin.main.domain.UserRoomConsume;
import com.erban.admin.main.dto.UserConsumeDTO;
import com.erban.admin.main.mapper.UserRoomConsumeConfDAO;
import com.erban.admin.main.mapper.UserRoomConsumeDAO;
import com.erban.admin.main.mapper.UserRoomTaskDAO;
import com.erban.main.service.room.RoomService;
import com.xchat.common.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/9 11:23
 */
@Component
public class UserRoomTask {

    @Autowired
    private UserRoomTaskDAO userRoomTaskDAO;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserRoomConsumeConfDAO userRoomConsumeConfDAO;
    @Autowired
    private UserRoomConsumeDAO userRoomConsumeDAO;

    @Scheduled(cron = "0 0 7 * * ?")
    public void countUserConsume() {
        //
        List<Long> roomList = userRoomConsumeConfDAO.listRoomUid();
        if (roomList == null) {
            return;
        }
        String endDate = DateUtil.date2Str(new Date(), DateUtil.DateFormat.YYYY_MM_DD);
        String beginDate = DateUtil.date2Str(DateUtil.addDay(new Date(), - 1), DateUtil.DateFormat.YYYY_MM_DD);
        roomList.forEach(uid -> {
            List<UserConsumeDTO> consumeList = userRoomTaskDAO.countUserConsumeByRoom(uid, beginDate, endDate);
            consumeList.forEach(dto -> {
                UserRoomConsume roomConsume = new UserRoomConsume();
                roomConsume.setRoomUid(dto.getRoomUid());
                roomConsume.setUid(dto.getUid());
                roomConsume.setFirstDate(dto.getMinDate());
                roomConsume.setLastDate(dto.getMaxDate());
                roomConsume.setTotalNum(dto.getTotalNum());
                roomConsume.setTotalGold(dto.getTotalGold());
                userRoomConsumeDAO.saveConsume(roomConsume);
            });
        });
    }
}
