package com.erban.main.service.record;

import com.erban.main.model.BillRecord;
import com.erban.main.model.Gift;
import com.erban.main.model.UserPacketRecord;
import com.erban.main.model.Users;
import com.erban.main.service.gift.GiftService;
import com.erban.main.service.user.UserPacketRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.ExpendRecordVo;
import com.erban.main.vo.GainRecordVo;
import com.erban.main.vo.UserPacketRecordVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.GetTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class PersonBillRecordService {

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private UserPacketRecordService userPacketRecordService;

    private List<Map<String, Object>> converBillRecordToGainVoList(List<BillRecord> billRecordList, Users users) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        Collections.sort(billRecordList);
        List<GainRecordVo> gainRecordVos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(billRecordList)) {
            return list;
        }
        for (int i = 0; i < billRecordList.size(); i++) {
            GainRecordVo gainRecordVo = conveBillRecordGainToVo(billRecordList.get(i), users);
            if (gainRecordVo == null) {
                continue;
            }
            if (i != 0) {
                boolean flag = isNowDay(billRecordList.get(i - 1), billRecordList.get(i));
                if (!flag) {
                    Date nowDate = GetTimeUtils.getTimesnights(billRecordList.get(i - 1).getCreateTime(), 0);
                    map.put(nowDate.getTime() + "", gainRecordVos);
                    list.add(map);
                    map = Maps.newHashMap();
                    gainRecordVos = Lists.newArrayList();
                }
            }
            gainRecordVos.add(gainRecordVo);
        }
        if (CollectionUtils.isEmpty(map)) {
            map.put(GetTimeUtils.getTimesnights(billRecordList.get(billRecordList.size() - 1).getCreateTime(), 0).getTime() + "", gainRecordVos);
            list.add(map);
        }
        return list;
    }

    private boolean isNowDay(BillRecord billRecord, BillRecord billRecord1) {
        Date data = GetTimeUtils.getTimesnights(billRecord.getCreateTime(), 0);
        if (billRecord1.getCreateTime().getTime() >= data.getTime()) {
            return true;
        }
        return false;
    }

    private List<Map<String, Object>> converBillRecordToExpendVoList(List<BillRecord> billRecordList, Users users) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        List<ExpendRecordVo> expendRecordVos = Lists.newArrayList();
        Collections.sort(billRecordList);
        if (CollectionUtils.isEmpty(billRecordList)) {
            return list;
        }
        for (int i = 0; i < billRecordList.size(); i++) {
            ExpendRecordVo expendRecordVo = conveBillRecordToExpendVo(billRecordList.get(i), users);
            if (expendRecordVo == null) {
                continue;
            }
            if (i != 0) {
                boolean flag = isNowDay(billRecordList.get(i - 1), billRecordList.get(i));
                if (!flag) {
                    Date nowDate = GetTimeUtils.getTimesnights(billRecordList.get(i - 1).getCreateTime(), 0);
                    map.put(nowDate.getTime() + "", expendRecordVos);
                    list.add(map);
                    map = Maps.newHashMap();
                    expendRecordVos = Lists.newArrayList();
                }
            }
            expendRecordVos.add(expendRecordVo);
        }
        if (CollectionUtils.isEmpty(map)) {
            map.put(GetTimeUtils.getTimesnights(billRecordList.get(billRecordList.size() - 1).getCreateTime(), 0).getTime() + "", expendRecordVos);
            list.add(map);
        }
        return list;
    }

    private GainRecordVo conveBillRecordGainToVo(BillRecord billRecord, Users users) {
        GainRecordVo gainRecordVo = new GainRecordVo();
        Users targetUser = usersService.getUsersByUid(billRecord.getTargetUid());
        if (billRecord.getObjType().equals(Constant.BillType.giftIncome)) {
            if (null == billRecord.getGiftId()) {
                return null;
            }
            gainRecordVo.setTargetNick(targetUser.getNick());
            Gift gift = giftService.getGiftById(billRecord.getGiftId());
            gainRecordVo.setGiftPic(gift.getPicUrl());
            gainRecordVo.setGiftName(gift.getGiftName());
            gainRecordVo.setGiftNum(billRecord.getGiftNum());
            gainRecordVo.setRecordTime(billRecord.getCreateTime());
        } else if (billRecord.getObjType().equals(Constant.BillType.getCash)) {
//            if (!billRecord.getBillStatus().equals(Constant.WithDraw.finish)) {
//                return null;
//            }
            gainRecordVo.setGoldNum(billRecord.getGoldNum());
            gainRecordVo.setRecordTime(billRecord.getUpdateTime());
            gainRecordVo.setMoney(billRecord.getMoney());
        }
        gainRecordVo.setDiamondNum(billRecord.getDiamondNum());
        gainRecordVo.setGainType(billRecord.getObjType());
        return gainRecordVo;
    }

    private ExpendRecordVo conveBillRecordToExpendVo(BillRecord billRecord, Users users) {
        ExpendRecordVo expendRecordVo = new ExpendRecordVo();
        Users targetUser = usersService.getUsersByUid(billRecord.getTargetUid());
        if (billRecord.getObjType().equals(Constant.BillType.giftPay)) {
            if (null == billRecord.getGiftId()) {
                return null;
            }
            expendRecordVo.setTargetNick(targetUser.getNick());
            Gift gift = giftService.getGiftById(billRecord.getGiftId());
            expendRecordVo.setGiftPic(gift.getPicUrl());
            expendRecordVo.setGiftNum(billRecord.getGiftNum());
            expendRecordVo.setGiftName(gift.getGiftName());
        } else if (billRecord.getObjType().equals(Constant.BillType.charge)) {
            expendRecordVo.setMoney(billRecord.getMoney());
        } else if (billRecord.getObjType().equals(Constant.BillType.orderIncome)) {
            expendRecordVo.setUserAvatar(targetUser.getAvatar());
            expendRecordVo.setUserNick(targetUser.getNick());
            expendRecordVo.setTargetAvatar(users.getAvatar());
            expendRecordVo.setTargetNick(users.getNick());
            expendRecordVo.setDiamoundNum(billRecord.getDiamondNum());
        } else if (billRecord.getObjType().equals(Constant.BillType.orderPay)) {
            expendRecordVo.setUserAvatar(users.getAvatar());
            expendRecordVo.setUserNick(users.getNick());
            expendRecordVo.setTargetAvatar(targetUser.getAvatar());
            expendRecordVo.setTargetNick(targetUser.getNick());
        }
        expendRecordVo.setGoldNum(billRecord.getGoldNum());
        expendRecordVo.setRecordTime(billRecord.getCreateTime());
        expendRecordVo.setExpendType(billRecord.getObjType());
        return expendRecordVo;
    }

    public BusiResult getBillRecordAllList(Long uid, Integer pageSize, Integer pageNo, Long time, Byte type) {
        if (pageSize == null) {
            pageSize = 50;
        }
        if (pageNo == null) {
            pageNo = 1;
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = usersService.getUsersByUid(uid);
        Integer pageCount = null;
        List<BillRecord> billRecordList = null;
        pageNo = (pageNo - 1) * pageSize;
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newLinkedHashMap();
        if (time != null) {
            Date date = new Date(time);
            Integer recordCount = billRecordService.getRecordCountByDate(uid, Constant.BillStartTime.startTime, GetTimeUtils.getTimesnights(date, 24), type);
            pageCount = (recordCount + pageSize - 1) / pageSize;
            billRecordList = billRecordService.getRecordByDateList(uid, Constant.BillStartTime.startTime, GetTimeUtils.getTimesnights(date, 24), pageNo, pageSize, type);
        } else {
            Integer recordCount = billRecordService.getRecordCount(uid, type);
            pageCount = (recordCount + pageSize - 1) / pageSize;
            billRecordList = billRecordService.getRecordAllList(uid, pageNo, pageSize, type);
        }
        if (type.equals(Constant.BillType.charge) || type.equals(Constant.BillType.giftPay) || type.equals(Constant.BillType.orderPay)) {
            list = converBillRecordToExpendVoList(billRecordList, users);
        } else {
            list = converBillRecordToGainVoList(billRecordList, users);
        }
        map.put("billList", list);
        map.put("pageCount", pageCount);
        busiResult.setData(map);
        return busiResult;
    }

    public BusiResult getRedPacketRecord(Long uid, Integer pageNo, Integer pageSize, Long time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (pageSize == null) {
            pageSize = 50;
        }
        if (pageNo == null) {
            pageNo = 1;
        }
        Map<String, Object> map = Maps.newHashMap();
        List<Map<String, Object>> list = Lists.newArrayList();
        List<UserPacketRecord> userPacketRecords = null;
        Integer pageCount = null;
        pageNo = (pageNo - 1) * pageSize;
        if (time != null) {
            Date date = new Date(time);
            Integer recordCount = userPacketRecordService.getRecordCountByDate(uid, Constant.BillStartTime.startTime, GetTimeUtils.getTimesnights(date, 24));
            pageCount = (recordCount + pageSize - 1) / pageSize;
            userPacketRecords = userPacketRecordService.getRecordByDateList(uid, Constant.BillStartTime.startTime, GetTimeUtils.getTimesnights(date, 24), pageNo, pageSize);
        } else {
            Integer recordCount = userPacketRecordService.getRecordCount(uid);
            pageCount = (recordCount + pageSize - 1) / pageSize;
            userPacketRecords = userPacketRecordService.getRecordAllList(uid, pageNo, pageSize);
        }
        list = converBillRecordToPacketVoList(userPacketRecords);
        map.put("billList", list);
        map.put("pageCount", pageCount);
        busiResult.setData(map);
        return busiResult;
    }

    private List<Map<String, Object>> converBillRecordToPacketVoList(List<UserPacketRecord> userPacketRecords) {
        List<Map<String, Object>> list = Lists.newArrayList();
        List<UserPacketRecordVo> userPacketRecordVos = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userPacketRecords)) {
            return list;
        }
        for (int i = 0; i < userPacketRecords.size(); i++) {
            UserPacketRecordVo userPacketRecordVo = conveBillRecordToPacketVo(userPacketRecords.get(i));
            if (userPacketRecordVo == null) {
                continue;
            }
            if (i != 0) {
                boolean flag = isNowDayByPacket(userPacketRecords.get(i - 1), userPacketRecords.get(i));
                if (!flag) {
                    Date nowDate = GetTimeUtils.getTimesnights(userPacketRecords.get(i - 1).getCreateTime(), 0);
                    map.put(nowDate.getTime() + "", userPacketRecordVo);
                    list.add(map);
                    map = Maps.newHashMap();
                    userPacketRecordVos = Lists.newArrayList();
                }
            }
            userPacketRecordVos.add(userPacketRecordVo);
        }
        if (CollectionUtils.isEmpty(map)) {
            map.put(GetTimeUtils.getTimesnights(userPacketRecords.get(userPacketRecords.size() - 1).getCreateTime(), 0).getTime() + "", userPacketRecordVos);
            list.add(map);
        }
        return list;
    }

    private UserPacketRecordVo conveBillRecordToPacketVo(UserPacketRecord userPacketRecord) {
        UserPacketRecordVo userPacketRecordVo = new UserPacketRecordVo();
        userPacketRecordVo.setUid(userPacketRecord.getUid());
        userPacketRecordVo.setType(userPacketRecord.getType());
        userPacketRecordVo.setPacketNum(userPacketRecord.getPacketNum());
        userPacketRecordVo.setCreateTime(userPacketRecord.getCreateTime());
        return userPacketRecordVo;
    }

    private boolean isNowDayByPacket(UserPacketRecord userPacketRecord, UserPacketRecord userPacketRecord1) {
        Date data = GetTimeUtils.getTimesnights(userPacketRecord.getCreateTime(), 0);
        if (userPacketRecord1.getCreateTime().getTime() >= data.getTime()) {
            return true;
        }
        return false;
    }
}
