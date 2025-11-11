package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRecord;
import com.erban.main.model.NobleRes;

import java.util.Date;
import java.util.List;

public interface NobleRecordMapperMgr {

    List<NobleRecord> getNobleRecordByDate(long uid, Date date, int skip, int size);
}
