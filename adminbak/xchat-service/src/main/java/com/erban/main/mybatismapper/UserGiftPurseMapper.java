package com.erban.main.mybatismapper;

import com.erban.main.model.UserGiftPurse;
import com.erban.main.model.UserGiftPurseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserGiftPurseMapper {
    int countByExample(UserGiftPurseExample example);

    int deleteByExample(UserGiftPurseExample example);

    int deleteByPrimaryKey(Integer giftPurseId);

    int insert(UserGiftPurse record);

    int insertSelective(UserGiftPurse record);

    List<UserGiftPurse> selectByExample(UserGiftPurseExample example);

    UserGiftPurse selectByPrimaryKey(Integer giftPurseId);

    int updateByExampleSelective(@Param("record") UserGiftPurse record, @Param("example") UserGiftPurseExample example);

    int updateByExample(@Param("record") UserGiftPurse record, @Param("example") UserGiftPurseExample example);

    int updateByPrimaryKeySelective(UserGiftPurse record);

    int updateByPrimaryKey(UserGiftPurse record);
}
