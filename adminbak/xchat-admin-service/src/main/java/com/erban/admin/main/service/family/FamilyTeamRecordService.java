package com.erban.admin.main.service.family;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.FamilySendGiftRecordDTO;
import com.erban.admin.main.mapper.FamilyGiftRecordMapper;
import com.erban.main.model.FamilyTeamRecord;
import com.erban.main.model.FamilyTeamRecordExample;
import com.erban.main.model.RoomFlowWeekVo;
import com.erban.main.mybatismapper.FamilyTeamRecordMapper;
import com.erban.main.param.admin.FamilyFlowParam;
import com.erban.main.vo.admin.StatFamilyFlowVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author chris
 * @Title:
 * @date 2018/10/30
 * @time 15:04
 */
@Service
public class FamilyTeamRecordService {

    @Autowired
    private FamilyTeamRecordMapper familyTeamRecordMapper;

    @Autowired
    private FamilyGiftRecordMapper familyGiftRecordMapper;

    @Autowired
    private JedisService jedisService;


    public BusiResult getList(FamilyFlowParam familyFlowParam) {
        AtomicReference<Long> totalGold= new AtomicReference<>(0L);
        if(StringUtils.isNotBlank(familyFlowParam.getBeginDate()) && StringUtils.isNotBlank(familyFlowParam.getEndDate())) {
            familyFlowParam.setBeginDate(familyFlowParam.getBeginDate() + " 00:00:00");
            familyFlowParam.setEndDate(familyFlowParam.getEndDate() + " 23:59:59");
        }
        List<StatFamilyFlowVo> statRoomFlowVos = familyTeamRecordMapper.statFamilyFlow(familyFlowParam);
        List<StatFamilyFlowVo> ret = new ArrayList<>();
        if (statRoomFlowVos == null || statRoomFlowVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }
        statRoomFlowVos.stream().forEach(item -> {
            item.setTotal(String.valueOf(item.getNum()));
            totalGold.set(totalGold.get() + item.getNum());
            String startTime = DateTimeUtil.date2Str(DateTimeUtil.getLastDay(DateTimeUtil.convertStrToDate(item.getYears(),"yyyy-MM-dd"),7), DateUtil.DateFormat.YYYY_MM_DD) + " 00:00:00";
            String endTime = item.getYears() + " 23:59:59";
            item.setConsumers(familyGiftRecordMapper.countByConsumers(item.getFamilyId(),startTime,endTime));
        });
        int beginSize =familyFlowParam.getSize() * (familyFlowParam.getPage() - 1);
        int endSize = familyFlowParam.getSize() * familyFlowParam.getPage();
        for (int size = beginSize; size < (endSize<=statRoomFlowVos.size()?endSize:statRoomFlowVos.size()); size++) {
            ret.add(statRoomFlowVos.get(size));
        }
        //总流水
        ret.get(0).setTotalGold(totalGold.get().doubleValue());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", statRoomFlowVos.size());
        jsonObject.put("rows", fillingWeekProportion(ret));
        return new BusiResult(BusiStatus.SUCCESS, jsonObject);
    }



    /**
     * 添加 周流水环比
     * @param roomFlowVoList 流水记录
     * @return
     */
    public List<StatFamilyFlowVo> fillingWeekProportion(List<StatFamilyFlowVo> roomFlowVoList){
        Gson gson = new Gson();
        // 使用定时任务周一更新缓存
        String result = "";
        RoomFlowWeekVo rfvo;
        for (StatFamilyFlowVo vo : roomFlowVoList) {
            result = jedisService.hget(RedisKey.family_flow_proportion.getKey(), vo.getUid().toString());
            result = Optional.ofNullable(result).orElse("");
            if (StringUtils.isBlank(result)) {
                // 刷新缓存
                refreshWeekRoomFlowCache();
                result = jedisService.hget(RedisKey.family_flow_proportion.getKey(), vo.getUid().toString());
                if (StringUtils.isBlank(result)) {
                    vo.setProportion("0");
                }
            } else {
                rfvo = gson.fromJson(result, RoomFlowWeekVo.class);
                vo.setProportion(rfvo.getProportion());
            }
        }
        return roomFlowVoList;
    }


    /**
     * 刷新房间周流水缓存
     */
    public void refreshWeekRoomFlowCache() {
        Gson gson = new Gson();
        jedisService.del(RedisKey.family_flow_proportion.getKey());
        // 记录更新时间
        jedisService.hset(RedisKey.family_flow_proportion.getKey(), "-1", DateUtil.date2Str(new Date(), DateUtil.DateFormat.YYYY_MM_DD));
        List<RoomFlowWeekVo> flowWeekVos = familyTeamRecordMapper.sumByTowWeeks();
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
            jedisService.hset(RedisKey.family_flow_proportion.getKey(), uid == null ? "0" : vo.getUid().toString(), gson.toJson(vo));
        }
    }

    public BusiResult selectByList(Long teamId, Integer page, Integer size){
        List<FamilySendGiftRecordDTO> familySendGiftRecordDTOS = new ArrayList<>();
        try {
            FamilyTeamRecordExample familyTeamRecordExample = new FamilyTeamRecordExample();
            familyTeamRecordExample.createCriteria().andTeamIdEqualTo(teamId);
            FamilyTeamRecord familyTeamRecord =  familyTeamRecordMapper.selectByExample(familyTeamRecordExample).get(0);
            String startTime = DateTimeUtil.date2Str(Optional.ofNullable(DateTimeUtil.getLastDay(DateTimeUtil.convertStrToDate(familyTeamRecord.getYears(),"yyyy-MM-dd"),7))
                    .orElse(new Date()), DateUtil.DateFormat.YYYY_MM_DD) + " 00:00:00";
            String endTime = familyTeamRecord.getYears() + " 23:59:59";
            PageHelper.startPage(page, size);
            familySendGiftRecordDTOS = familyGiftRecordMapper.selectByList(teamId,startTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(familySendGiftRecordDTOS));
    }
}


