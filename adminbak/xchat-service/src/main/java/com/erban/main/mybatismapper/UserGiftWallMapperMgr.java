package com.erban.main.mybatismapper;

import com.erban.main.model.UserGiftWall;
import com.erban.main.model.UserGiftWallExample;

import java.util.List;

public interface UserGiftWallMapperMgr {
    List<UserGiftWall> getUserWallListByUidOrderByCount(Long uid);
    List<UserGiftWall> getUserWallListByUidOrderByGoldPrice(Long uid);
    int updateGiftWallCount(UserGiftWall userGiftWall);
}
