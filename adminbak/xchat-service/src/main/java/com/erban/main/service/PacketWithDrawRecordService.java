package com.erban.main.service;

import com.erban.main.model.PacketWithDrawRecord;
import com.erban.main.mybatismapper.PacketWithDrawRecordMapper;
import com.xchat.common.UUIDUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PacketWithDrawRecordService {

    @Autowired
    private PacketWithDrawRecordMapper packetWithDrawRecordMapper;

    public void insertInPacketWithDrawRecord(Long uid, Integer packetId, Double money, Byte create) {
        PacketWithDrawRecord packetWithDrawRecord = new PacketWithDrawRecord();
        packetWithDrawRecord.setRecordId(UUIDUitl.get());
        packetWithDrawRecord.setCreateTime(new Date());
        packetWithDrawRecord.setUid(uid);
        packetWithDrawRecord.setPacketProdCashId(packetId);
        packetWithDrawRecord.setPacketNum(money);
        packetWithDrawRecord.setRecordStatus(create);
        packetWithDrawRecordMapper.insert(packetWithDrawRecord);
    }
}
