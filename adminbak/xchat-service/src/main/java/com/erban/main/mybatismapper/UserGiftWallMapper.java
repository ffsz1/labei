package com.erban.main.mybatismapper;

import com.erban.main.model.UserGiftWall;
import com.erban.main.model.UserGiftWallExample;
import java.util.List;

public interface UserGiftWallMapper {
    int deleteByExample(UserGiftWallExample example);

    int deleteByPrimaryKey(Long giftWallId);

    int insert(UserGiftWall record);

    int insertSelective(UserGiftWall record);

    List<UserGiftWall> selectByExample(UserGiftWallExample example);

    UserGiftWall selectByPrimaryKey(Long giftWallId);

    int updateByPrimaryKeySelective(UserGiftWall record);

    int updateByPrimaryKey(UserGiftWall record);
}
