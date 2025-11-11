package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomCtrbSum;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatRoomCtrbSumMapperMgr {

    List<StatRoomCtrbSum> getStatRoomCtrbSumListByUid(Long uid);
    List<StatRoomCtrbSum> getRoomCharismaList(Map<String,Object> param);
    List<StatRoomCtrbSum> getRoomWealthList(Map<String,Object> param);
    int addSumGoldByCtrbId(Map<String,Object> param);

}
