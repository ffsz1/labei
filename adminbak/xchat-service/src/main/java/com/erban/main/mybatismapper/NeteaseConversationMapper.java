package com.erban.main.mybatismapper;

import com.erban.main.model.NeteaseConversation;
import com.erban.main.model.NeteaseConversationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NeteaseConversationMapper {
    int countByExample(NeteaseConversationExample example);

    int deleteByExample(NeteaseConversationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NeteaseConversation record);

    int insertSelective(NeteaseConversation record);

    List<NeteaseConversation> selectByExample(NeteaseConversationExample example);

    NeteaseConversation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NeteaseConversation record, @Param("example") NeteaseConversationExample example);

    int updateByExample(@Param("record") NeteaseConversation record, @Param("example") NeteaseConversationExample example);

    int updateByPrimaryKeySelective(NeteaseConversation record);

    int updateByPrimaryKey(NeteaseConversation record);
}
