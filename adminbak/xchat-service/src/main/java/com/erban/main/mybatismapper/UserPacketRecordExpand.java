package com.erban.main.mybatismapper;

import com.erban.main.model.UserPacketRecord;

import java.util.Date;
import java.util.List;

public interface UserPacketRecordExpand {

    Integer getRecordCountByDate(Long uid, Date startTime, Date timesnights);

    List<UserPacketRecord> getRecordByDateList(Long uid, Date startTime, Date timesnights, Integer pageNo, Integer pageSize);

    Integer getRecordCount(Long uid);

    List<UserPacketRecord> getRecordAllList(Long uid, Integer pageNo, Integer pageSize);

    List<UserPacketRecord> getRecordByDate(Long uid, Date date, Integer page, Integer size);

    List<UserPacketRecord> getDepositRecordByDate(Long uid, Date date, Integer page, Integer size);
}
