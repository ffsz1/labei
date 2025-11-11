package com.erban.main.service.gift;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.GiftSendRecordMapper;
import com.erban.main.mybatismapper.GiftSendRecordMapperExpand;
import com.erban.main.service.base.BaseService;
import com.erban.main.util.StringUtils;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.GetTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by 北岭山下 on 2017/8/8.
 */
@Service
public class GiftSendRecordService extends BaseService{

    @Autowired
    GiftSendRecordMapperExpand giftSendRecordMapperExpand;
    @Autowired
    private GiftSendRecordMapper giftSendRecordMapper;
    @Autowired
    private BillRecordMapper billRecordMapper;

    public List<GiftSendRecordVo> getGiftSendRecordVoList() {
        return giftSendRecordMapperExpand.getGiftSendRecordVoList();
    }

    public GiftSendRecord insertGiftSendRecord(GiftSendRecord giftSendRecord) {
        giftSendRecordMapper.insertSelective(giftSendRecord);
        return giftSendRecord;
    }

    public GiftSendRecord insertGiftInfo(Long uid, Long targetUid,Long roomUid,Byte roomType, int type, Integer giftId
            , Integer giftNum,Long totalGoldNum,double totalDiamondNum) {
        GiftSendRecord giftSendRecord = new GiftSendRecord();
        giftSendRecord.setGiftId(giftId);
        giftSendRecord.setReciveUid(targetUid);
        giftSendRecord.setRoomUid(roomUid);
        giftSendRecord.setRoomType(roomType);
        giftSendRecord.setGiftNum(giftNum);
        giftSendRecord.setSendEnv((byte) type);
        giftSendRecord.setUid(uid);
        giftSendRecord.setTotalGoldNum(totalGoldNum);
        giftSendRecord.setTotalDiamondNum(totalDiamondNum);
        giftSendRecord.setCreateTime(new Date());
        giftSendRecordMapper.insertSelective(giftSendRecord);
        return giftSendRecord;
    }

    public List<GiftSendRecord> getGiftList(Long roomUid) {
        GiftSendRecordExample example = new GiftSendRecordExample();
        example.createCriteria().andReciveUidEqualTo(roomUid).andSendEnvEqualTo((byte) Constant.SendGiftType.room).andCreateTimeBetween(GetTimeUtils.getTimesWeekmorning(), GetTimeUtils.getTimesWeeknight());
        List<GiftSendRecord> giftSendRecordList = giftSendRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(giftSendRecordList)) {
            return null;
        }
        return giftSendRecordList;
    }

    public List<GiftSendRecordVo2> getGiftSendRecordVo2List(Date startTime,Date endTime){
        return giftSendRecordMapperExpand.getTotalDiamondListByDate(startTime,endTime);
    }

    /**
     * 获取经验值
     * @param uid
     * @return
     */
    public Long getLevelExerpence(Long uid) {
        String exerValue = jedisService.hget(RedisKey.user_level_exper.getKey(), uid.toString());
        if (StringUtils.isBlank(exerValue)) {
            //从数据库获取
            GiftSendRecordVo3 totalGoldNum = giftSendRecordMapperExpand.getTotalGoldNumByUid(uid);
            if (totalGoldNum != null && totalGoldNum.getTotalGoldNum() != null) {
                jedisService.hincrBy(RedisKey.user_level_exper.getKey(), uid.toString(), totalGoldNum.getTotalGoldNum());//经验值
                return totalGoldNum.getTotalGoldNum();
            }else {
                jedisService.hincrBy(RedisKey.user_level_exper.getKey(), uid.toString(), 0L);
            }
        } else {
            return Long.valueOf(exerValue);
        }
        return Long.valueOf(0);
    }

    /**
     * 获取魅力值
     * @param receivedUid
     * @return
     */
    public Long getLevelCharm(Long receivedUid) {
        String charmValue = jedisService.hget(RedisKey.user_level_charm.getKey(), receivedUid.toString());
        if (StringUtils.isBlank(charmValue)) {
            //从数据库获取
            GiftSendRecordVo3 totalGoldNum = giftSendRecordMapperExpand.getTotalGoldNumByReceiveUid(receivedUid);
            if (totalGoldNum != null && totalGoldNum.getTotalGoldNum() != null) {
                jedisService.hincrBy(RedisKey.user_level_charm.getKey(), receivedUid.toString(), totalGoldNum.getTotalGoldNum());//魅力值
                return totalGoldNum.getTotalGoldNum();
            }else {
                jedisService.hincrBy(RedisKey.user_level_charm.getKey(), receivedUid.toString(), 0L);
            }
        } else {
            return Long.valueOf(charmValue);
        }
        return Long.valueOf(0);
    }
}
