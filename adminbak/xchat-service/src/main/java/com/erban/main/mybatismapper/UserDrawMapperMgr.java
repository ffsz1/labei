package com.erban.main.mybatismapper;

import com.erban.main.model.UserDraw;
import com.erban.main.model.UserDrawExample;
import com.erban.main.model.UserDrawRecord;
import com.erban.main.model.UserDrawRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDrawMapperMgr {
    List<UserDrawRecord> getUserDrawRecordListByStatus();
}
