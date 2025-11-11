package com.erban.main.mybatismapper;

import com.erban.main.model.NeteaseChatroom;
import com.erban.main.model.NeteaseChatroomExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NeteaseChatroomMapper {
    int countByExample(NeteaseChatroomExample example);

    int deleteByExample(NeteaseChatroomExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NeteaseChatroom record);

    int insertSelective(NeteaseChatroom record);

    List<NeteaseChatroom> selectByExample(NeteaseChatroomExample example);

    NeteaseChatroom selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NeteaseChatroom record, @Param("example") NeteaseChatroomExample example);

    int updateByExample(@Param("record") NeteaseChatroom record, @Param("example") NeteaseChatroomExample example);

    int updateByPrimaryKeySelective(NeteaseChatroom record);

    int updateByPrimaryKey(NeteaseChatroom record);
}
