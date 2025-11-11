package com.erban.main.mybatismapper;

import com.erban.main.model.UserGiftBonusPerDay;
import com.erban.main.model.UserGiftBonusPerDayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserGiftBonusPerDayMapper {
    int countByExample(UserGiftBonusPerDayExample example);

    int deleteByExample(UserGiftBonusPerDayExample example);

    int deleteByPrimaryKey(Integer bonusId);

    int insert(UserGiftBonusPerDay record);

    int insertSelective(UserGiftBonusPerDay record);

    List<UserGiftBonusPerDay> selectByExample(UserGiftBonusPerDayExample example);

    UserGiftBonusPerDay selectByPrimaryKey(Integer bonusId);

    int updateByExampleSelective(@Param("record") UserGiftBonusPerDay record, @Param("example") UserGiftBonusPerDayExample example);

    int updateByExample(@Param("record") UserGiftBonusPerDay record, @Param("example") UserGiftBonusPerDayExample example);

    int updateByPrimaryKeySelective(UserGiftBonusPerDay record);

    int updateByPrimaryKey(UserGiftBonusPerDay record);
}
