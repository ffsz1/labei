package com.erban.main.mybatismapper;

import com.erban.main.model.WXMsgPicInfo;
import com.erban.main.model.WXMsgPicInfoExample;
import java.util.List;

public interface WXMsgPicInfoMapper {
    int deleteByPrimaryKey(String wxMsgId);

    int insert(WXMsgPicInfo record);

    int insertSelective(WXMsgPicInfo record);

    List<WXMsgPicInfo> selectByExample(WXMsgPicInfoExample example);

    WXMsgPicInfo selectByPrimaryKey(String wxMsgId);

    int updateByPrimaryKeySelective(WXMsgPicInfo record);

    int updateByPrimaryKey(WXMsgPicInfo record);
}
