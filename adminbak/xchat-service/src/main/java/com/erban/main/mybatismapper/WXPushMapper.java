package com.erban.main.mybatismapper;

import com.erban.main.model.WXPush;
import com.erban.main.model.WXPushExample;
import java.util.List;

public interface WXPushMapper {
    int deleteByPrimaryKey(String wxPushId);

    int insert(WXPush record);

    int insertSelective(WXPush record);

    List<WXPush> selectByExample(WXPushExample example);

    WXPush selectByPrimaryKey(String wxPushId);

    int updateByPrimaryKeySelective(WXPush record);

    int updateByPrimaryKey(WXPush record);
}
