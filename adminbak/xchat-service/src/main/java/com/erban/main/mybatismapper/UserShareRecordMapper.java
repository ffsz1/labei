package com.erban.main.mybatismapper;

import com.erban.main.model.UserShareRecord;
import com.erban.main.model.UserShareRecordExample;
import java.util.List;

public interface UserShareRecordMapper {
    int deleteByPrimaryKey(String shareId);

    int insert(UserShareRecord record);

    int insertSelective(UserShareRecord record);

    List<UserShareRecord> selectByExample(UserShareRecordExample example);

    UserShareRecord selectByPrimaryKey(String shareId);

    int updateByPrimaryKeySelective(UserShareRecord record);

    int updateByPrimaryKey(UserShareRecord record);
}
