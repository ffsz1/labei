package com.erban.admin.main.service.room;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.room.conf.DrawGiftKey;
import com.erban.main.dto.RoomFlowEchartsDTO;
import com.erban.main.dto.RoomFlowEchartsParam;
import com.erban.main.model.RoomFlowWeekVo;
import com.erban.main.mybatismapper.GiftSendRecordMapperExpand;
import com.erban.main.mybatismapper.UserConfigureMapper;
import com.erban.main.param.admin.RoomFlowParam;
import com.erban.main.service.room.RoomFlowServie;
import com.erban.main.util.ListUtil;
import com.erban.main.vo.RoomFlowVo;
import com.erban.main.vo.admin.StatRoomFlowVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.common.util.DESUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台管理-钻石提现service  create by zhaomiao 2018/2/28
 */
@Service
public class StatRoomTotalFlowAdminService extends BaseService {
    @Value("${statFlowLink}")
    private String statFlowLink;

    @Value("${eggFlowLink}")
    private String eggFlowLink;

    @Value("${statFlowSecret}")
    private String statFlowSecret;
    @Autowired
    private GiftSendRecordMapperExpand giftSendRecordMapperExpand;

    @Autowired
    private RoomFlowServie roomFlowServie;

    @Autowired
    private UserConfigureMapper userConfigureMapper;


//    /**
//     *
//     * @param
//     * @return
//     */
//    public BusiResult getList(RoomFlowParam roomFlowParam) throws Exception {
//        Long totalGold=0L;
//        Double totalBouns=0D;
//        List<StatRoomFlowVo> ret = new ArrayList<>();
//        roomFlowParam.setBeginDate(roomFlowParam.getBeginDate()+" 00:00:00");
//        roomFlowParam.setEndDate(roomFlowParam.getEndDate()+" 23:59:59");
////        PageHelper.startPage(roomFlowParam.getPage(), roomFlowParam.getSize());
//        List<StatRoomFlowVo> statRoomFlowVos = giftSendRecordMapperExpand.statRoomFlow(roomFlowParam);
//        if (statRoomFlowVos == null || statRoomFlowVos.size() == 0) {
//            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
//        }
//        for (StatRoomFlowVo statRoomFlowVo : statRoomFlowVos) {
//            String secretUid = DESUtils.DESAndBase64Encrypt(statRoomFlowVo.getUid().toString(), statFlowSecret);
//            statRoomFlowVo.setRoomFlowLink(statFlowLink + secretUid);
//            double bouns = statRoomFlowVo.getBounsPersent() * statRoomFlowVo.getTotalGoldNum() / 1000.00;
//            statRoomFlowVo.setBouns(bouns);
//            totalGold = totalGold + statRoomFlowVo.getTotalGoldNum();
//            totalBouns = totalBouns + statRoomFlowVo.getBouns();
//        }
//        int beginSize =roomFlowParam.getSize() * (roomFlowParam.getPage() - 1);
//        int endSize = roomFlowParam.getSize() * roomFlowParam.getPage();
//        for (int size = beginSize; size < (endSize<=statRoomFlowVos.size()?endSize:statRoomFlowVos.size()); size++) {
//            ret.add(statRoomFlowVos.get(size));
//        }
//        ret.get(0).setTotalGold(totalGold/10.00);//总流水
//        ret.get(0).setTotalBouns(totalBouns);//总分成
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("total", statRoomFlowVos.size());
//        jsonObject.put("rows", ret);
//        return new BusiResult(BusiStatus.SUCCESS, jsonObject);
//    }

    /** 交友房 100441 是测试服的 */
    private final List<Long> uidList = Lists.newArrayList(100441L, 1006657L, 1018397L, 1006660L);

    /**
     *
     * @param
     * @return
     */
    public BusiResult getList(RoomFlowParam roomFlowParam) throws Exception {
        Long totalGold=0L;
        Double totalBouns=0D;
        List<StatRoomFlowVo> ret = new ArrayList<>();
        roomFlowParam.setBeginDate(roomFlowParam.getBeginDate()+" 00:00:00");
        roomFlowParam.setEndDate(roomFlowParam.getEndDate()+" 23:59:59");
        String name = roomFlowParam.getSortName();
        name = StringUtils.isBlank(name) ? "totalGoldNum " : name ;
        String order = roomFlowParam.getSortOrder();
        order = StringUtils.isBlank(order) ? "desc" : order;
        roomFlowParam.setOrderByClauser(name + " " + order);
//        PageHelper.startPage(roomFlowParam.getPage(), roomFlowParam.getSize());
        List<StatRoomFlowVo> statRoomFlowVos = giftSendRecordMapperExpand.statRoomFlow(roomFlowParam);
        if (statRoomFlowVos == null || statRoomFlowVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }
        List<Long> uidList = userConfigureMapper.selectByUid();
        for (StatRoomFlowVo statRoomFlowVo : statRoomFlowVos) {
            String secretUid = DESUtils.DESAndBase64Encrypt(statRoomFlowVo.getUid().toString(), statFlowSecret);
            if(uidList == null || uidList.size() == 0){
                String statFlowLinks = "<a target='_blank' href='"+statFlowLink + secretUid+"'>"+statFlowLink + secretUid+"</a>";
                statRoomFlowVo.setRoomFlowLink(statFlowLinks);
                statRoomFlowVo.setTotal(String.valueOf(statRoomFlowVo.getTotalGoldNum()));
            }else {
                if(uidList.contains(statRoomFlowVo.getUid())){
                    String statFlowLinks = "<a target='_blank' href='" + statFlowLink + secretUid + "'>" + statFlowLink + secretUid + "</a>";
                    String eggFlowLinks = "<a target='_blank' href='" + eggFlowLink + secretUid + "'>" + eggFlowLink + secretUid + "</a>";
                    statRoomFlowVo.setRoomFlowLink(statFlowLinks + "\n" + eggFlowLinks);
                     Long total = giftSendRecordMapperExpand.countStatRoomTotalFlow(statRoomFlowVo.getUid());
                    statRoomFlowVo.setTrickTreatTotal(total);
                    String totalStr = statRoomFlowVo.getTotalGoldNum() + "\n  拉贝:" + statRoomFlowVo.getTrickTreatTotal();
                    statRoomFlowVo.setTotal(totalStr);
                }else{
                    String statFlowLinks = "<a target='_blank' href='" + statFlowLink + secretUid + "'>" + statFlowLink + secretUid + "</a>";
                    statRoomFlowVo.setRoomFlowLink(statFlowLinks);
                    statRoomFlowVo.setTotal(String.valueOf(statRoomFlowVo.getTotalGoldNum()));
                }
            }
            double bouns = statRoomFlowVo.getBounsPersent() * statRoomFlowVo.getTotalGoldNum() / 1000.00;
            statRoomFlowVo.setBouns(bouns);
            totalGold = totalGold + statRoomFlowVo.getTotalGoldNum();
            totalBouns = totalBouns + statRoomFlowVo.getBouns();
        }
        PageInfo<StatRoomFlowVo> page = new PageInfo<>(statRoomFlowVos);
        statRoomFlowVos = ListUtil.page(statRoomFlowVos, roomFlowParam.getPage(), roomFlowParam.getSize());
        JSONObject jsonObject = new JSONObject();
        if (!statRoomFlowVos.isEmpty()) {
            statRoomFlowVos.get(0).setTotalGold(totalGold/10.00);//总流水
            statRoomFlowVos.get(0).setTotalBouns(totalBouns);//总分成
        }
        jsonObject.put("total", page.getTotal());
        jsonObject.put("rows", fillingWeekProportion(statRoomFlowVos));
//        jsonObject.put("drawGift", drawGift());
        // jsonObject.put("rows", ret);
        return new BusiResult(BusiStatus.SUCCESS, jsonObject);
    }

    public Map<String, Object> drawGift() {
        Map<String, Object> map = Maps.newHashMap();
        String total = jedisService.hget(RedisKey.gift_draw_num.getKey(), DrawGiftKey.total.toString());
        String output = jedisService.hget(RedisKey.gift_draw_output.getKey(), DrawGiftKey.total.toString());
        String giftNum = jedisService.hget(RedisKey.gift_draw_output.getKey(), DrawGiftKey.full_gift.toString());
        long userNum = jedisService.hlen(RedisKey.gift_draw_output.getKey());
        map.put("totalGold", total);
        map.put("outputGold", output);
        map.put("giftNum", giftNum);
        map.put("userNum", userNum - 2);
        return map;
    }

    /**
     * 添加 房间周流水环比
     * @param roomFlowVoList 房间流水记录
     * @return
     */
    public List<StatRoomFlowVo> fillingWeekProportion(List<StatRoomFlowVo> roomFlowVoList){
        // 使用定时任务周一更新缓存
        String result = "";
        RoomFlowWeekVo rfvo;
        for (StatRoomFlowVo vo : roomFlowVoList) {
            result = jedisService.hget(RedisKey.room_flow_proportion.getKey(), vo.getUid().toString());
            if (StringUtils.isBlank(result)) {
                vo.setProportion("0");
                // 刷新缓存
            } else {
                rfvo = gson.fromJson(result, RoomFlowWeekVo.class);
                vo.setProportion(rfvo.getProportion());
            }
        }
        return roomFlowVoList;
    }

    /**
     * 导出到Excel表格
     *
     * @param
     * @return
     */
    public BusiResult exportExcel(RoomFlowParam roomFlowParam) throws Exception {
        roomFlowParam.setBeginDate(roomFlowParam.getBeginDate()+" 00:00:00");
        roomFlowParam.setEndDate(roomFlowParam.getEndDate()+" 23:59:59");
        List<StatRoomFlowVo> statRoomFlowVos = giftSendRecordMapperExpand.statRoomFlow(roomFlowParam);
        if (statRoomFlowVos == null || statRoomFlowVos.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_EXPORTEXCEL);
        }
        fillingWeekProportion(statRoomFlowVos);

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("房间流水");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("房间ID");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("房主昵称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("电话");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("房间类型");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("房间标签");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("流水明细链接");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("流水(金币)");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("分成比例(%)");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("分成(元)");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("支付宝账号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("渠道");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("银行卡");
        cell.setCellStyle(style);
        cell = row.createCell((short) 13);
        cell.setCellValue("持卡人");
        cell.setCellStyle(style);
        cell = row.createCell((short) 14);
        cell.setCellValue("消费人数(总)");
        cell.setCellStyle(style);
        cell = row.createCell((short) 15);
        cell.setCellValue("消费总人数(新)");
        cell.setCellStyle(style);
        cell = row.createCell((short) 16);
        cell.setCellValue("周流水环比");
        cell.setCellStyle(style);

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < statRoomFlowVos.size(); i++) {
            row = sheet.createRow((int) i + 1);
            StatRoomFlowVo statRoomFlowVo = statRoomFlowVos.get(i);
            String secretUid = DESUtils.DESAndBase64Encrypt(statRoomFlowVo.getUid().toString(), statFlowSecret);
            statRoomFlowVo.setRoomFlowLink(statFlowLink + secretUid);
            double bouns = statRoomFlowVo.getBounsPersent() * statRoomFlowVo.getTotalGoldNum() / 1000.00;
            statRoomFlowVo.setBouns(bouns);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue(statRoomFlowVo.getErbanNo());
            row.createCell((short) 1).setCellValue(statRoomFlowVo.getNick());
            row.createCell((short) 2).setCellValue(statRoomFlowVo.getPhone());
            row.createCell((short) 3).setCellValue(statRoomFlowVo.getIsPermitRoom().toString().equals("1")?"牌照":statRoomFlowVo.getIsPermitRoom().toString().equals("3")?"审核牌照":"无");
            row.createCell((short) 4).setCellValue(statRoomFlowVo.getRoomTag());
            row.createCell((short) 5).setCellValue(statRoomFlowVo.getRoomFlowLink());
            row.createCell((short) 6).setCellValue(statRoomFlowVo.getTotalGoldNum());
            row.createCell((short) 7).setCellValue(statRoomFlowVo.getBounsPersent());
            row.createCell((short) 8).setCellValue(statRoomFlowVo.getBouns());
            row.createCell((short) 9).setCellValue(statRoomFlowVo.getAlipayAccount());
            row.createCell((short) 10).setCellValue(statRoomFlowVo.getAlipayAccountName());
            row.createCell((short) 11).setCellValue(statRoomFlowVo.getOccupation());
            row.createCell((short) 12).setCellValue(statRoomFlowVo.getBankCard());
            row.createCell((short) 13).setCellValue(statRoomFlowVo.getCardholder());
            row.createCell((short) 14).setCellValue(statRoomFlowVo.getCountUsers());
            row.createCell((short) 15).setCellValue(statRoomFlowVo.getCountNewUsers());
            row.createCell((short) 16).setCellValue(statRoomFlowVo.getProportion());
        }
        return new BusiResult(BusiStatus.SUCCESS, wb);
    }

    public Map<String, Object> getOneMonthFlow(Long uid) {
        RoomFlowEchartsParam rfep = new RoomFlowEchartsParam();
        rfep.setUid(uid);
        rfep.setClassType(RoomFlowEchartsParam.DATE_TYPE_DAY);
        Date now = new Date();
        // 一个月
        int count = 30;
        Date beginDate = DateUtil.addDay(now, - count);
        String beginDateStr = DateUtil.date2Str(beginDate, DateUtil.DateFormat.YYYY_MM_DD);
        String endDate = DateUtil.date2Str(now, DateUtil.DateFormat.YYYY_MM_DD);
        rfep.setBeginDate(beginDateStr);
        rfep.setEndDate(endDate);
        // 查询记录
        List<RoomFlowEchartsDTO>  list = giftSendRecordMapperExpand.roomFlow(rfep);
        List<String> xAxis = Lists.newArrayList();
        List<Long> data = Lists.newArrayList();
        String dateStr = "";
        int index = 0;
        RoomFlowEchartsDTO echartsDTO = list.get(index);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat fmts = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < count; i ++) {
            now = DateUtil.addDay(beginDate, i);
            dateStr = fmt.format(now);
            String days = fmts.format(now);
            xAxis.add(days);
            if (dateStr.equals(echartsDTO.getPeriod())) {
                data.add(echartsDTO.getTotalGold());
                index ++;
                if (index < list.size()) {
                    echartsDTO = list.get(index);
                }
            } else {
                // 设置默认值
                data.add(0L);
            }
        }
//        for (RoomFlowEchartsDTO dto : list) {
//            xAxis.add(dto.getPeriod());
//            data.add(dto.getTotalGold());
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("xAxis", xAxis);
        map.put("name", list.isEmpty() ? "" : list.get(0).getNick());
        return map;
    }

    public Map<String, Object> echarts(Integer classType, String uids, String beginDate, String endDate, String minNum) {
        RoomFlowEchartsParam rfep = new RoomFlowEchartsParam();
        if (com.erban.main.util.StringUtils.isNotBlank(beginDate)) {
            rfep.setBeginDate(beginDate + "00:00:00");
        }
        if (com.erban.main.util.StringUtils.isNotBlank(endDate)) {
            rfep.setEndDate(endDate + "23:59:59");
        }
        rfep.setClassType(classType);
        rfep.setMinNum(minNum);
        if(com.erban.main.util.StringUtils.isNotBlank(uids)) {
            String[] uidArr = uids.split(",");
            List<Long> list = Lists.newArrayList();
            for (String id : uidArr) {
                list.add(Long.parseLong(id));
            }
            rfep.setUidList(list);
        }
        List<RoomFlowEchartsDTO> list = giftSendRecordMapperExpand.roomFlow(rfep);
        Map<String, Integer> periodMap = new HashMap<>();
        List<String> nameList = Lists.newArrayList();
        String period = "";
        Integer periodIndex;
        int index = 0;
        for (RoomFlowEchartsDTO dto: list) {
            period = dto.getPeriod();
            periodIndex = periodMap.get(period);
            if (periodIndex == null) {
                periodMap.put(period, index);
                nameList.add(period);
                index ++;
            }
        }
        Map<String, RoomFlowVo> resultMap = new HashMap<>();
        String name;
        RoomFlowVo vo;
        Long[] data;
        int periodSize = periodMap.size();
        for (RoomFlowEchartsDTO dto: list) {
            period = dto.getPeriod();
            index = periodMap.get(period);
            name = dto.getUid().toString();
            vo = resultMap.get(name);
            if (vo == null) {
                vo = new RoomFlowVo();
                data = new Long[periodSize];
                data[index] = dto.getTotalGold();
                vo.setData(data);
                vo.setName(name);
                resultMap.put(name, vo);
            } else {
                vo.getData()[index] = dto.getTotalGold();
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", resultMap.values());
        map.put("names", nameList);
        return map;
    }


}
