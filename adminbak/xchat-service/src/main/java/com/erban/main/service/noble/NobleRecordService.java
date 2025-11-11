package com.erban.main.service.noble;

import com.erban.main.model.NobleRecord;
import com.erban.main.model.NobleRecordExample;
import com.erban.main.model.NobleRes;
import com.erban.main.mybatismapper.NobleRecordMapper;
import com.erban.main.mybatismapper.NobleRecordMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.vo.BillRecordExpandVo;
import com.erban.main.vo.BillSearchVo;
import com.erban.main.vo.noble.NobleRecordVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.constant.Constant;
import com.xchat.common.utils.DateTimeUtil;
import org.easymock.internal.matchers.Compare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class NobleRecordService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(NobleRecordService.class);

    @Autowired
    private NobleRecordMapper nobleRecordMapper;
    @Autowired
    private NobleRecordMapperMgr nobleRecordMapperMgr;

    public boolean isExistMessId(String messId) {
        NobleRecordExample example = new NobleRecordExample();
        example.createCriteria().andMessIdEqualTo(messId);
        int count = nobleRecordMapper.countByExample(example);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public int addNobleRecord(NobleRecord nobleRecord) {
        return nobleRecordMapper.insertSelective(nobleRecord);
    }

    public List<NobleRecord> getNobleRecordByDate(long uid, long date, int pageNo, Integer pageSize) {
        Date nextDate = DateTimeUtil.getNextDay(new Date(date), 1);
        return nobleRecordMapperMgr.getNobleRecordByDate(uid, nextDate, (pageNo - 1) * pageSize, pageSize);
    }

    public List<LinkedHashMap<Long, List<NobleRecordVo>>> transToRecordVo(List<NobleRecord> list) {
        LinkedHashMap<Long, List<NobleRecordVo>> voMap = Maps.newLinkedHashMap();
        for (NobleRecord record : list) {
            try {
                Date date1 = DateTimeUtil.getDate(record.getCreateTime());
                List<NobleRecordVo> voList = voMap.get(date1.getTime());
                if (voList == null) {
                    voList = Lists.newArrayList();
                    voMap.put(date1.getTime(), voList);
                }
                NobleRecordVo searchVo = new NobleRecordVo();
                searchVo.setRecordTime(record.getCreateTime().getTime());
                searchVo.setUid(record.getUid());
                if (record.getOptType().equals(Constant.NobleOptType.open)) {
                    searchVo.setOptStr("开通" + record.getNobleName() + "贵族");
                } else {
                    searchVo.setOptStr("续费" + record.getNobleName() + "贵族");
                }
                if (record.getPayType().equals(Constant.NoblePayType.gold)){
                    searchVo.setPayStr("-" + record.getGoldNum() + "金币");
                } else {
                    searchVo.setPayStr("-" + record.getMoney() + "元");
                }
                    voList.add(searchVo);
            } catch (Exception e) {
                logger.error("transToRecordVo error", e);
            }
        }

        List<LinkedHashMap<Long, List<NobleRecordVo>>> reList = Lists.newArrayList();
        Set<Long> keySet = voMap.keySet();
        for (Long key : keySet) {
            LinkedHashMap<Long, List<NobleRecordVo>> map = Maps.newLinkedHashMap();
            map.put(key, voMap.get(key));
            reList.add(map);
        }
        return reList;
    }

    public static void main(String[] args) {
        Date date = DateTimeUtil.getNextDay(new Date(), 1);

        System.out.println(date.getTime());
    }
}
