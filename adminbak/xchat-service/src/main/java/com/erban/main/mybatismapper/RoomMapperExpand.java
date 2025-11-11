package com.erban.main.mybatismapper;

import com.erban.main.model.Room;
import com.erban.main.vo.SearchVo;

import java.util.List;
import java.util.Map;

public interface RoomMapperExpand {

    List<SearchVo> searchRoomByKey(Map<String, Object> param);

    List<Room> selectNewRooms(Map<String, Object> param);

    List<Room> selectRadioRooms(Map<String, Object> param);

    List<Room> selectPoolRooms();
}
