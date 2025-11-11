package com.erban.main.service.room;

import com.erban.main.model.GiftSendRecordVo3;
import com.erban.main.model.GiftSendRecordVo4;
import com.erban.main.model.RoomFlowWeekVo;
import com.erban.main.mybatismapper.GiftSendRecordMapperExpand;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 房间流水service
 */
@Service
public class RoomFlowServie {

    @Autowired
    private GiftSendRecordMapperExpand giftSendRecordMapperExpand;

    @Autowired
    protected JedisService jedisService;

    private Gson gson = new Gson();

    public BusiResult getOne(Long roomUid){
        List<GiftSendRecordVo3> giftSendRecordVo3s = giftSendRecordMapperExpand.getRoomFlowGroupBy(roomUid);
        if(giftSendRecordVo3s==null || giftSendRecordVo3s.size()==0){
            return new BusiResult(BusiStatus.STAT_ROOMFLOW_NOTEXIT);
        }
        List<GiftSendRecordVo3> rets=new ArrayList<>(giftSendRecordVo3s.size());

        for (int i =0; i<giftSendRecordVo3s.size() ; i++) {

            rets.add(giftSendRecordVo3s.get(i));
            if(i==giftSendRecordVo3s.size()-1) break;;
            long time1 = giftSendRecordVo3s.get(i).getCreateTime().getTime();
            long time2 = giftSendRecordVo3s.get(i+1).getCreateTime().getTime();
            long n = (time1 - time2) / (24 * 60 * 60 * 1000);
            if (n > 1) {//间隔n-1天
                for (int j = 1; j <= n-1; j++) {
                    GiftSendRecordVo3 giftSendRecordVo3 = new GiftSendRecordVo3();
                    giftSendRecordVo3.setCreateTime(new Date(time1 - 24 * 60 * 60 * 1000 * j));
                    giftSendRecordVo3.setTotalGoldNum(0L);
                    giftSendRecordVo3.setDate(DateTimeUtil.convertDate(giftSendRecordVo3.getCreateTime(), "yyyy-MM-dd"));
                    rets.add(giftSendRecordVo3);
                }
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, rets);
    }

    public BusiResult getDetail(String date, Long roomUid){
        String beginDate=date+" 00:00:00";
        String endDate=date+" 23:59:59";
        List<GiftSendRecordVo4> roomFlowDetail = giftSendRecordMapperExpand.getRoomFlowDetail(beginDate, endDate, roomUid);
        if(roomFlowDetail==null || roomFlowDetail.size()==0){
            return new BusiResult(BusiStatus.STAT_ROOMFLOW_DETAILNOTEXIT);
        }
        return new BusiResult(BusiStatus.SUCCESS,roomFlowDetail);
    }

    /**
     * 刷新房间周流水缓存
     */
    public void refreshWeekRoomFlowCache() {
        // 本周的 上两周的 周一
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -2 * 7);
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        jedisService.del(RedisKey.room_flow_proportion.getKey());
        // 记录更新时间
        jedisService.hset(RedisKey.room_flow_proportion.getKey(), "-1", DateUtil.date2Str(new Date(), DateUtil.DateFormat.YYYY_MM_DD));
        List<RoomFlowWeekVo> flowWeekVos = giftSendRecordMapperExpand.sumByTowWeeks(null, null);
        Long uid = 0L;
        for (RoomFlowWeekVo vo : flowWeekVos) {
            long first = vo.getFirstWeek();
            long second = vo.getSecondWeek();
            double pro;
            if (second == 0) {
                pro = first;
            } else {
                pro = ((double) first - second) / second;
            }
            NumberFormat nf = NumberFormat.getNumberInstance();
            // 保留四位小数
            nf.setMaximumFractionDigits(4);
            nf.setGroupingUsed(false);
            vo.setProportion(nf.format(pro));
            vo.setCreateDate(new Date());
            uid = vo.getUid();
            jedisService.hset(RedisKey.room_flow_proportion.getKey(), uid == null ? "0" : vo.getUid().toString(), gson.toJson(vo));
        }
    }
}
