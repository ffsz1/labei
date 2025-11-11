package com.erban.admin.main.service.room;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.Room;
import com.erban.main.model.RoomRecomPool;
import com.erban.main.model.RoomRecomPoolExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomMapperExpand;
import com.erban.main.mybatismapper.RoomRecomPoolMapper;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RoomAdminService extends BaseService {

    @Autowired
    private RoomMapperExpand roomMapperExpand;
    @Autowired
    private RoomRecomPoolMapper roomRecomPoolMapper;
    @Autowired
    private UsersService usersService;

    public void addRoomPools(String[] erbanNoArr) {
        for (int i = 0; i < erbanNoArr.length; i++) {
            try {
                RoomRecomPool roomRecomPool = new RoomRecomPool();
                Users users = usersService.getUsersByErBanNo(Long.valueOf(erbanNoArr[i].trim()));
                if (users == null) {
                    continue;
                }
                roomRecomPool.setUid(users.getUid());
                roomRecomPool.setCreateTime(new Date());
                roomRecomPoolMapper.insert(roomRecomPool);
            } catch (Exception e) {
                logger.error("addRooms error, erban: " + erbanNoArr[i]);
            }
        }
    }


    public int delRoomPool(Long uid) {
        return roomRecomPoolMapper.deleteByPrimaryKey(uid);
    }

    public int delAllRoomPools() {
        return roomRecomPoolMapper.deleteByExample(new RoomRecomPoolExample());
    }

    public PageInfo<Room> getRoomPoolByPage(int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Room> list = roomMapperExpand.selectPoolRooms();
        return new PageInfo<>(list);
    }


}
