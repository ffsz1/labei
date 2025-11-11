package com.erban.main.service.user;

import com.erban.main.model.UserPacketRecord;
import com.erban.main.mybatismapper.UserPacketRecordExpand;
import com.erban.main.mybatismapper.UserPacketRecordMapper;
import com.xchat.common.UUIDUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserPacketRecordService {

    @Autowired
    private UserPacketRecordMapper userPacketRecordMapper;

    @Autowired
    private UserPacketRecordExpand userPacketRecordExpand;

    @Autowired
    private UserPacketService userPacketService;

    public void insertInPacketRecord(Long uid, Double money, Byte withDraw, Byte create) {
        UserPacketRecord userPacketRecord = new UserPacketRecord();
        userPacketRecord.setPacketId(UUIDUitl.get());
        userPacketRecord.setCreateTime(new Date());
        userPacketRecord.setPacketNum(money);
        userPacketRecord.setPacketStatus(create);
        userPacketRecord.setType(withDraw);
        userPacketRecord.setUid(uid);
        userPacketRecordMapper.insertSelective(userPacketRecord);
    }

    /**
     *生成一条用户红包记录
     * @param uid
     * @param type 红包动作类型
     * @param randomPacketNum 随机红包金额
     * @param objId 目标ID
     * @param srcUid 分享用户房间
     */
    public void savePacketRecordByAction(Long uid,Byte type,double randomPacketNum,String objId,Long srcUid){
        Date date=new Date();
        UserPacketRecord userPacketRecord=new UserPacketRecord();
        userPacketRecord.setPacketId(UUIDUitl.get());
        userPacketRecord.setPacketNum(randomPacketNum);
        userPacketRecord.setUid(uid);
        userPacketRecord.setObjId(objId);
        userPacketRecord.setType(type);
        userPacketRecord.setHasUnpack(false);
        userPacketRecord.setCreateTime(date);
        userPacketRecord.setSrcUid(srcUid);
        userPacketRecordMapper.insert(userPacketRecord);
    }

    public Integer getRecordCountByDate(Long uid, Date startTime, Date timesnights) {
        Integer recordCount = userPacketRecordExpand.getRecordCountByDate(uid, startTime, timesnights);
        return recordCount;
    }

    public List<UserPacketRecord> getRecordByDateList(Long uid, Date startTime, Date timesnights, Integer pageNo, Integer pageSize) {
        List<UserPacketRecord> userPacketRecords = userPacketRecordExpand.getRecordByDateList(uid, startTime, timesnights, pageNo, pageSize);
        return userPacketRecords;
    }

    public Integer getRecordCount(Long uid) {
        Integer recordCount = userPacketRecordExpand.getRecordCount(uid);
        return recordCount;
    }

    public List<UserPacketRecord> getRecordAllList(Long uid, Integer pageNo, Integer pageSize) {
        List<UserPacketRecord> userPacketRecords = userPacketRecordExpand.getRecordAllList(uid,pageNo, pageSize);
        return userPacketRecords;
    }
}
