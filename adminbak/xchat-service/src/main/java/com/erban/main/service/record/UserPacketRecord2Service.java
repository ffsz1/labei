package com.erban.main.service.record;

import com.erban.main.model.UserPacketRecord;
import com.erban.main.mybatismapper.UserPacketRecordExpand;
import com.erban.main.vo.BillSearchVo;
import com.erban.main.vo.PacketRecordVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserPacketRecord2Service {

    @Autowired
    private UserPacketRecordExpand userPacketRecordExpand;


    public List<UserPacketRecord> getUserPacketRecord(long uid, long date, int page, int size) {
        Date nextDate = DateTimeUtil.getNextDay(new Date(date), 1);
        return userPacketRecordExpand.getRecordByDate(uid, nextDate, (page - 1) * size, size);
    }

    /**
     *  获取提现红包的记录
     *
     * @param uid
     * @param date
     * @param page
     * @param size
     * @return
     */
    public List<UserPacketRecord> getPacketDeposiRecord(long uid, long date, int page, int size) {
        Date nextDate = DateTimeUtil.getNextDay(new Date(date), 1);
        return userPacketRecordExpand.getDepositRecordByDate(uid, nextDate, (page - 1) * size, size);
    }

    public List<LinkedHashMap<Long, List<PacketRecordVo>>> transToSearchVo(List<UserPacketRecord> list) {
        LinkedHashMap<Long, List<PacketRecordVo>> voMap = Maps.newLinkedHashMap();
        for (UserPacketRecord record : list) {
            Date date1 = DateTimeUtil.getDate(record.getCreateTime());
            List<PacketRecordVo> voList = voMap.get(date1.getTime());
            if (voList == null) {
                voList = Lists.newArrayList();
                voMap.put(date1.getTime(), voList);
            }
            PacketRecordVo searchVo = new PacketRecordVo();
            searchVo.setDate(date1.getTime());
            searchVo.setRecordTime(record.getCreateTime().getTime());
            searchVo.setPacketNum(record.getPacketNum());
            searchVo.setTypeStr(transTypeStr(record.getType()));
            searchVo.setUid(record.getUid());
            voList.add(searchVo);
        }
        List<LinkedHashMap<Long, List<PacketRecordVo>>> reList = Lists.newArrayList();
        Set<Long> keySet = voMap.keySet();
        for (Long key : keySet) {
            LinkedHashMap<Long, List<PacketRecordVo>> map = Maps.newLinkedHashMap();
            map.put(key, voMap.get(key));
            reList.add(map);
        }
        return reList;
    }

    /**
     * 转换红包类型为文字的格式
     *
     * @param type 红包类型,1申请成为分享小能手，新人红包 2分享动作 分享红包，3分享后用户注册  邀请红包，4注册用户充值抽成 分成红包
     * @return
     */
    public String transTypeStr(int type) {
        switch (type) {
            case 1:
                return "新人红包";
            case 2:
                return "分享红包";
            case 3:
                return "邀请红包";
            case 4:
                return "分成红包";
            case 5:
                return "提现红包";
        }
        return "其它红包";
    }
}
