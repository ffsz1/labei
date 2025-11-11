package com.erban.main.service;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuguofu on 2017/10/8.
 */
@Service
public class HotRoomPermitService extends BaseService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RobotService robotService;

    /**
     * 房间添加牌照，更新DB的牌照房字段为1，并且通知云信下掉机器人
     *
     * @param erbanNo
     * @return
     */
    public BusiResult addHotRoomPermitService(Long erbanNo) {
        boolean result = updatePermitRoomByErbanNo(erbanNo, 1);
        if (result) {
            return new BusiResult(BusiStatus.SUCCESS);
        } else {
            return new BusiResult(BusiStatus.USERNOTEXISTS, "操作失败，可能原因，" + GlobalConfig.appName + "号不存在，请检查！"
                    , null);
        }
    }

    /**
     * 房间删除牌照，更新DB的牌照房字段为2，并且通知云信下掉机器人
     *
     * @param erbanNo
     * @return
     */
    public BusiResult deleteHotRoomPermitService(Long erbanNo) {
        boolean result = updatePermitRoomByErbanNo(erbanNo, 2);
        if (result) {
            return new BusiResult(BusiStatus.SUCCESS);
        } else {
            return new BusiResult(BusiStatus.USERNOTEXISTS, "操作失败，可能原因，" + GlobalConfig.appName + "号不存在，请检查！"
                    , null);
        }
    }

    /**
     * 更新房间的牌照信息，通知云信更新机器人的信息
     *
     * @param erbanNo
     * @param operType
     * @return
     */
    public boolean updatePermitRoomByErbanNo(Long erbanNo, Integer operType) {
        Users user = usersService.getUsresByErbanNo(erbanNo);
        if (user == null) {
            return false;
        }
        // 更新房间的牌照状态
        Room room = new Room();
        room.setUid(user.getUid());
        room.setIsPermitRoom(operType.byteValue());
        roomMapper.updateByPrimaryKeySelective(room);

        // 缓存房间的信息
        room = roomMapper.selectByPrimaryKey(user.getUid());
        jedisService.hwrite(RedisKey.room.getKey(), user.getUid().toString(), gson.toJson(room));

        if (operType == 1) {
            // 添加机器人
            robotService.addRobotRelaByAddPermitRoom(user.getUid(), room.getRoomId());
        } else {
            // 删除机器人
            robotService.deleteRobotRelaByRemovePermitRoom(user.getUid());
        }
        return true;
    }
}
