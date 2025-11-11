package com.erban.admin.main.service.room;

import com.erban.main.dto.RoomBgDTO;
import com.erban.main.dto.RoomConfListDTO;
import com.erban.main.model.Room;
import com.erban.main.model.RoomConf;
import com.erban.main.model.RoomConfExample;
import com.erban.main.mybatismapper.RoomConfMapper;
import com.erban.main.service.im.ImRoomManager;
import com.erban.main.service.im.dto.RoomDTO;
import com.erban.main.service.room.RoomBgPicService;
import com.erban.main.service.room.RoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomConfService {
    @Autowired
    private RoomConfMapper roomConfMapper;
    @Autowired
    private ImRoomManager imroomManager;
    @Autowired
    private RoomService roomService;
    @Autowired
    private JedisService jedisService;

    @Autowired
    private RoomBgPicService roomBgPicService;

    public void save(RoomConf roomConf) throws Exception {
        roomConfMapper.insertOrUpdateSelective(roomConf);
        Room room = roomService.getRoomByUid(roomConf.getRoomUid());

        RoomDTO roomDto = new RoomDTO();
        BeanUtils.copyProperties(room, roomDto);
        roomDto.setGiftDrawEnable(roomConf.getRoomType());
        jedisService.hset(RedisKey.room_conf.getKey(String.valueOf(room.getType())), String.valueOf(room.getUid()), new Gson().toJson(roomConf));
        if(room.getBackPic() != null && !"null".equalsIgnoreCase(room.getBackPic())){
            RoomBgDTO roomBgDTO = roomBgPicService.getById(Integer.valueOf(room.getBackPic()),room.getRoomId());
            if(roomBgDTO != null){
                roomDto.setBackPicUrl(roomBgDTO.getPicUrl());
            }
        }
        imroomManager.updateRoom(roomDto);
    }

    public PageInfo<RoomConfListDTO> listWithPage(String searchText, int page, int pageSize) {
        if (page <= 0) {
            page = 1;
        }

        if (pageSize <= 0) {
            pageSize = 10;
        }

        PageHelper.startPage(page, pageSize);
        List<RoomConfListDTO> list = roomConfMapper.listRoomConf(searchText);
        return new PageInfo<>(list);
    }

    public RoomConf getRoomConf(Long roomUid) {
        RoomConfExample example = new RoomConfExample();
        example.createCriteria().andRoomUidEqualTo(roomUid);
        List<RoomConf> list = roomConfMapper.selectByExample(example);
        return list == null || list.size() == 0 ? null : list.get(0);
    }
}
