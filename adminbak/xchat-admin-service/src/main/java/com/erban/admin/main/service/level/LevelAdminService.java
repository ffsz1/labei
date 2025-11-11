package com.erban.admin.main.service.level;

import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.GiftSendRecordVo3;
import com.erban.main.mybatismapper.GiftSendRecordMapperExpand;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 财富+魅力等级初始化服务
 */
@Service
public class LevelAdminService extends BaseService{
    @Autowired
    private GiftSendRecordMapperExpand giftSendRecordMapperExpand;
    @Autowired
    private JedisService jedisService;

    public BusiResult initLevelCash(){
        //财富等级
        List<GiftSendRecordVo3> totalGoldNumByUidList = giftSendRecordMapperExpand.getTotalGoldNumByUidList();
        List<GiftSendRecordVo3> totalGoldNumByReceiveUidList = giftSendRecordMapperExpand.getTotalGoldNumByReceiveUidList();
        for (GiftSendRecordVo3 vo3:totalGoldNumByUidList) {
            jedisService.hincrBy(RedisKey.user_level_exper.getKey(),vo3.getUid().toString(),vo3.getTotalGoldNum());
        }
        for (GiftSendRecordVo3 vo3:totalGoldNumByReceiveUidList) {
            jedisService.hincrBy(RedisKey.user_level_charm.getKey(),vo3.getUid().toString(),vo3.getTotalGoldNum());
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }
}
