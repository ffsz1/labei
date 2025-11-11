package com.juxiao.xchat.service.record.output.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.juxiao.xchat.base.utils.DESUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.output.bo.OutputValueParam;
import com.juxiao.xchat.dao.output.vo.OutputVO;
import com.juxiao.xchat.dao.output.vo.OutputValueVO;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.service.record.output.OutputValueService;
import com.juxiao.xchat.service.record.output.dto.HomeChannelDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 16:34
 */
@Service
public class OutputValueServiceImpl implements OutputValueService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UsersDao usersDao;

    private static final String STAT_FLOW_SECRET = "roomFlow";

    @Override
    public WebServiceMessage getList(OutputValueParam outputValueParam) {
        logger.info("[outPutValue]param{}", outputValueParam);
        if (StringUtils.isBlank(outputValueParam.getGroupId())) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        try {
            String groupId = DESUtils.DESAndBase64Decrypt(outputValueParam.getGroupId(), STAT_FLOW_SECRET);
            outputValueParam.setGroupId(groupId);
        }catch (Exception e){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        if (outputValueParam.getShowType() == null){
            return WebServiceMessage.failure(WebServiceCode.NOT_EXISTS);
        }
        try {
            if(outputValueParam.getLinkedmeChannel() != null && outputValueParam.getLinkedmeChannel() != ""){
                String channel = DESUtils.DESAndBase64Decrypt(outputValueParam.getLinkedmeChannel(), STAT_FLOW_SECRET);
//                outputValueParam.setLinkedmeChannel(channel);
                if (StringUtils.isNotBlank(channel) && StringUtils.isNumeric(channel)) {
                    outputValueParam.setShareUid(Long.valueOf(channel));
                }
            }
        }catch (Exception e){
            logger.error("[getList]出现异常,异常信息:{}", e);
            return WebServiceMessage.failure(WebServiceCode.NOT_EXISTS);
        }
        outputValueParam.setLinkedmeChannel(null);

        outputValueParam.setPageNum(outputValueParam.getPageSize() * outputValueParam.getPageNum());
        if(outputValueParam.getSignBegin() == null && outputValueParam.getSignEnd() == null){
            outputValueParam.setSignBegin(DateUtils.dateToStr(DateUtils.getLastDay(new Date(),6)));
            outputValueParam.setSignEnd(DateUtils.dateToStr(new Date()));
        }
        outputValueParam.setSignBegin(outputValueParam.getSignBegin() + " 00:00:00");
        outputValueParam.setSignEnd(outputValueParam.getSignEnd() + " 23:59:59");
        if(StringUtils.isNotBlank(outputValueParam.getMedium())){
            List<Long> uidList = Lists.newArrayList(outputValueParam.getMedium().split(","))
                    .stream().filter(s -> StringUtils.isNotBlank(s) && StringUtils.isNumeric(s))
                    .map(s -> Long.valueOf(s)).collect(Collectors.toList());
            outputValueParam.setShareUidList(uidList);
        }else{
            if (!"0".equals(outputValueParam.getGroupId())) {
                //
                List<Map<String, Object>> homeChannelDTOList = jdbcTemplate.queryForList(
                        "select id,channel from home_channel where type = 2 and group_id = ?", outputValueParam.getGroupId());
                List<Long> uidList = Lists.newArrayList();
                homeChannelDTOList.forEach(s -> {
                    if (s.get("channel") != null) {
                        String uid = s.get("channel").toString();
                        if (StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid)) {
                            uidList.add(Long.valueOf(uid));
                        }
                    }
                });
                outputValueParam.setShareUidList(uidList);
            }
        }
        /*
         * 之前的统计是通过 linkedme_channel, 后修改为 share_uid;
         */
        List<OutputValueVO> outputValueVoList = usersDao.selectByParam(outputValueParam);
        if(outputValueVoList==null||outputValueVoList.size()==0){
            // return WebServiceMessage.failure(WebServiceCode.NOT_EXISTS);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        String beginDate;
        Date endDate;
        long begin;
        BigDecimal num;
        BigDecimal sum;
        BigDecimal signNum=BigDecimal.ZERO;
        for(OutputValueVO outputValueVo:outputValueVoList) {
            begin = System.currentTimeMillis();
            beginDate = outputValueVo.getSignDate() + " 00:00:00";
            endDate = DateUtils.getLastDay(DateUtils.convertStrToDate(outputValueVo.getSignDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"), -13);
            if (outputValueParam.getShowType().intValue() == 1) {
                result = getRechargeNum(outputValueVo.getUidList(), beginDate, endDate);
                outputValueVo.setSumNum(getRechargeSun(outputValueVo.getUidList()));
            } else if (outputValueParam.getShowType().intValue() == 2) {
                result = getRechargeAmount(outputValueVo.getUidList(), beginDate, endDate);
                outputValueVo.setSumNum(getRechargeAmountSun(outputValueVo.getUidList()));
            } else if (outputValueParam.getShowType().intValue() == 3) {
                result = getRechargeAmount(outputValueVo.getUidList(), beginDate, endDate);
                signNum = new BigDecimal(outputValueVo.getSignNum());
                if(signNum.compareTo(BigDecimal.ZERO) == 0){
                    outputValueVo.setSumNum(BigDecimal.ZERO);
                }else{
                    outputValueVo.setSumNum(getRechargeAmountSun(outputValueVo.getUidList()).divide(signNum, 2, BigDecimal.ROUND_HALF_UP));
                }
            } else if (outputValueParam.getShowType().intValue() == 4) {
                result = getRechargeAmount(outputValueVo.getUidList(), beginDate, endDate);
                signNum = getRechargeSun(outputValueVo.getUidList());
                if(signNum.compareTo(BigDecimal.ZERO) == 0){
                    outputValueVo.setSumNum(BigDecimal.ZERO);
                }else{
                    outputValueVo.setSumNum(getRechargeAmountSun(outputValueVo.getUidList()).divide(signNum, 2, BigDecimal.ROUND_HALF_UP));
                }
            }else if(outputValueParam.getShowType().intValue() == 5){
                result = getLoginRechargeNum(outputValueVo.getUidList(), beginDate, endDate);
                outputValueVo.setSumNum(getLoginRechargeSun(outputValueVo.getUidList()));
            }

            sum = BigDecimal.ZERO;
            for(Map<String, Object> re:result) {
                num = new BigDecimal(re.get("num").toString());
                sum=sum.add(num);
                if (outputValueParam.getShowType().intValue() == 3) {
                    if(signNum.compareTo(BigDecimal.ZERO) == 0){
                        num = BigDecimal.ZERO;
                    }else{
                        num = sum.divide(signNum, 2, BigDecimal.ROUND_HALF_UP);
                    }
                }else if (outputValueParam.getShowType().intValue() == 4) {
                    signNum = getRechargeSun(outputValueVo.getUidList(), beginDate, re.get("chargeDate").toString()+" 23:59:59");
                    if(signNum.compareTo(BigDecimal.ZERO) == 0){
                        num = BigDecimal.ZERO;
                    }else{
                        num = sum.divide(signNum, 2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                switch (re.get("differenceDate").toString()) {
                    case "0":
                        outputValueVo.setFirstNum(num);
                        break;
                    case "1":
                        outputValueVo.setSecondNum(num);
                        break;
                    case "2":
                        outputValueVo.setThirdNum(num);
                        break;
                    case "3":
                        outputValueVo.setFourthNum(num);
                        break;
                    case "4":
                        outputValueVo.setFifthNum(num);
                        break;
                    case "5":
                        outputValueVo.setSixthNum(num);
                        break;
                    case "6":
                        outputValueVo.setSeventhNum(num);
                        break;
                    case "7":
                        outputValueVo.setEighthNum(num);
                        break;
                    case "8":
                        outputValueVo.setNinthNum(num);
                        break;
                    case "9":
                        outputValueVo.setTenthNum(num);
                        break;
                    case "10":
                        outputValueVo.setEleventhNum(num);
                        break;
                    case "11":
                        outputValueVo.setTwelfthNum(num);
                        break;
                    case "12":
                        outputValueVo.setThirteenthNum(num);
                        break;
                    case "13":
                        outputValueVo.setFourteenthNum(num);
                        break;
                    default:
                        break;
                }
            }
            System.out.println("耗时:" + (System.currentTimeMillis() - begin));
        }
        OutputVO outputVO = usersDao.countCharge(outputValueParam);
        outputVO.setList(outputValueVoList);
        outputVO.setRegisterNum(outputValueVoList.stream().mapToLong(OutputValueVO::getSignNum).sum());
        return WebServiceMessage.success(outputVO);
    }

    /**
     * 统计登陆人数
     * @param uidList 用户uid列表
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @TargetDataSource(name = "ds2")
    private List<Map<String,Object>> getLoginRechargeNum(String uidList, String beginDate, Date endDate) {
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(a.create_time, '%Y-%m-%d') as chargeDate,COUNT(DISTINCT a.uid) as num,TIMESTAMPDIFF(DAY,?,a.create_time) as differenceDate FROM account_login_record a force index(uid) WHERE a.uid in ("+uidList+") AND a.create_time BETWEEN ? and ? GROUP BY chargeDate ", beginDate, beginDate, endDate);
    }

    @TargetDataSource(name = "ds2")
    private List<Map<String, Object>> getRechargeNum(String uidList, String beginDate, Date endDate) {
        //  AND c.pingxx_charge_id IS NOT NULL
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(c.create_time, '%Y-%m-%d') as chargeDate, COUNT(DISTINCT c.uid) as num, TIMESTAMPDIFF(DAY,?,c.create_time) as differenceDate FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.create_time BETWEEN ? and ? GROUP BY chargeDate", beginDate, beginDate, endDate);
    }

    @TargetDataSource(name = "ds2")
    private List<Map<String, Object>> getRechargeAmount(String uidList, String beginDate, Date endDate) {
        //  AND c.pingxx_charge_id IS NOT NULL
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(c.create_time, '%Y-%m-%d') as chargeDate, sum(c.amount)/100 as num, TIMESTAMPDIFF(DAY,?,c.create_time) as differenceDate FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.create_time BETWEEN ? and ? GROUP BY chargeDate", beginDate, beginDate, endDate);
    }

    @TargetDataSource(name = "ds2")
    private BigDecimal getRechargeSun(String uidList) {
        // AND c.pingxx_charge_id IS NOT NULL
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' ", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

    @TargetDataSource(name = "ds2")
    private BigDecimal getRechargeSun(String uidList, String beginDate, String endDate) {
        // AND c.pingxx_charge_id IS NOT NULL
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.create_time BETWEEN ? and ?", BigDecimal.class, beginDate, endDate);
        return result==null?BigDecimal.ZERO:result;
    }

    @TargetDataSource(name = "ds2")
    private BigDecimal getRechargeAmountSun(String uidList) {
        // AND c.pingxx_charge_id IS NOT NULL
        BigDecimal result = jdbcTemplate.queryForObject("SELECT sum(c.amount)/100 FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2'", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

    /**
     * 统计登陆人
     * @param uidList 用户uid列表
     * @return
     */
    @TargetDataSource(name = "ds2")
    private BigDecimal getLoginRechargeSun(String uidList) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM account_login_record c force index(uid) WHERE c.uid in ("+uidList+") ", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

    @TargetDataSource(name = "ds2")
    @Override
    public WebServiceMessage getChannelList(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        try {
            groupId = DESUtils.DESAndBase64Decrypt(groupId, STAT_FLOW_SECRET);
        }catch (Exception e){
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        if ("0".equals(groupId)) {
            // 0 是默认值
            return WebServiceMessage.success(Lists.newArrayList());
        }
        List<Map<String, Object>>  homeChannelDTOList = jdbcTemplate.queryForList("select id,channel from home_channel where type = 2 and group_id = ?", groupId);
        List<HomeChannelDTO> channelDTOList = homeChannelDTOList.stream().map(m -> new HomeChannelDTO(m.getOrDefault("id", "").toString(),
                m.getOrDefault("channel", "").toString()))
                .collect(Collectors.toList());
        return WebServiceMessage.success(channelDTOList);
    }

    @Override
    public WebServiceMessage listRegister(OutputValueParam param) {
        String channel = "";
        String groupId = "";
        try {
            groupId = DESUtils.DESAndBase64Decrypt(param.getGroupId(), STAT_FLOW_SECRET);
            if (StringUtils.isNotBlank(param.getLinkedmeChannel())) {
                channel = DESUtils.DESAndBase64Decrypt(param.getLinkedmeChannel(), STAT_FLOW_SECRET);
            } else if (StringUtils.isNotBlank(param.getMedium())) {
                channel = param.getMedium();
            }
        } catch (Exception e) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        Date date = DateUtils.parser(param.getDate(), "yyy-MM-dd");
        if (date == null) {
            logger.error("[时间格式错误]");
            return WebServiceMessage.success(Lists.newArrayList());
        }
        Date endDate = org.apache.commons.lang.time.DateUtils.addDays(date, 1);
        String uids = listChannel(groupId);
        param.setPageNum(param.getPageNum() < 1 ? 1 : param.getPageNum());
        int begin = (param.getPageNum() - 1) * param.getPageSize();
        int end = param.getPageNum() * param.getPageSize();
        SQL sql2 = new SQL();
        sql2.SELECT("u.erban_no"," u.nick", " u.avatar", "u.create_time");
        sql2.FROM("users u");
        sql2.LEFT_OUTER_JOIN(" account a ON u.uid = a.uid ");
        sql2.WHERE("a.sign_time BETWEEN '"+ DateUtils.dateToStr(date) +"' and  '"+ DateUtils.dateToStr(endDate) +"' ");
        if (StringUtils.isNotBlank(channel)) {
            sql2.WHERE("u.share_uid = " + Long.valueOf(channel) );
        }
        if (StringUtils.isNotBlank(uids)) {
            sql2.WHERE("u.share_uid IN ("+ uids +")");
        }
        String sql = sql2.toString() + " LIMIT " + begin + "," + end;
        return WebServiceMessage.success(jdbcTemplate.queryForList(sql));
    }

    @Override
    public WebServiceMessage listCharge(OutputValueParam param) {
        String channel = "";
        String groupId = "";
        try {
            groupId = DESUtils.DESAndBase64Decrypt(param.getGroupId(), STAT_FLOW_SECRET);
            if (StringUtils.isNotBlank(param.getLinkedmeChannel())) {
                channel = DESUtils.DESAndBase64Decrypt(param.getLinkedmeChannel(), STAT_FLOW_SECRET);
            } else if (StringUtils.isNotBlank(param.getMedium())) {
                channel = param.getMedium();
            }
        } catch (Exception e) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        // 计算时间
        Date date1 = DateUtils.parser(param.getDate(), "yyyy-MM-dd");
        if (date1 == null) {
            logger.error("[时间格式错误]");
            return WebServiceMessage.success(Lists.newArrayList());
        }
        String uids = listChannel(groupId);
        // 开始时间
        date1 = org.apache.commons.lang.time.DateUtils.addDays(date1, param.getDays() - 1);
        Date endDate = org.apache.commons.lang.time.DateUtils.addDays(date1, 1);
        param.setPageNum(param.getPageNum() < 1 ? 1 : param.getPageNum());
        int begin = (param.getPageNum() - 1) * param.getPageSize();
        int end = param.getPageNum() * param.getPageSize();
        // 查询充值记录
        SQL sql1 = new SQL();
        sql1.SELECT("cr.create_time", "cr.amount / 100 AS amount", "u.nick", "u.erban_no");
        sql1.FROM("charge_record cr force index(uid) ");
        sql1.LEFT_OUTER_JOIN("users u ON u.uid = cr.uid");
        sql1.WHERE("cr.create_time BETWEEN '"+ DateUtils.dateToStr(date1) +"' and  '"+ DateUtils.dateToStr(endDate) +"' ");
        sql1.WHERE("cr.charge_status = '2'");
        if (StringUtils.isNotBlank(channel)) {
            sql1.WHERE("u.share_uid = " + Long.valueOf(channel) );
        }
        if (StringUtils.isNotBlank(uids)) {
            sql1.WHERE("u.share_uid IN ("+ uids +")");
        }
        String sql = sql1.toString() + " LIMIT " + begin + "," + end;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        // 查充值总数, 充值人数
        SQL sql2 = new SQL();
        sql2.SELECT("sum(cr.amount) / 100 totalAmount", "count(DISTINCT cr.uid) totalUser");
        sql2.FROM("charge_record cr force index(uid) ");
        sql2.LEFT_OUTER_JOIN("users u ON u.uid = cr.uid");
        sql2.WHERE("cr.charge_status = '2'");
        sql2.WHERE("cr.create_time BETWEEN '" + DateUtils.dateToStr(date1) + "' and  '"+ DateUtils.dateToStr(endDate) +"' ");
        if (StringUtils.isNotBlank(channel)) {
            sql2.WHERE("u.share_uid = " + Long.valueOf(channel) );
        }
        if (StringUtils.isNotBlank(uids)) {
            sql2.WHERE("u.share_uid IN ("+ uids +")");
        }
        Map<String, Object> forMap = jdbcTemplate.queryForMap(sql2.toString());
        if (forMap != null) {
            forMap.put("recordList", list);
        }
        return WebServiceMessage.success(forMap);
    }

    /**
     * 根据分组ID查询所有的渠道
     * @param groupId
     * @return
     */
    public String listChannel(String groupId) {
        Set<String> uids = Sets.newHashSet();
        if (!"0".equals(groupId)) {
            List<Map<String, Object>> homeChannelDTOList = jdbcTemplate.queryForList(
                    "select id,channel from home_channel where type = 2 and group_id = ?", groupId);
            homeChannelDTOList.stream().filter(s -> s.get("channel") != null).forEach(s -> {
                String uid = s.get("channel").toString();
                if (StringUtils.isNotBlank(uid) && StringUtils.isNumeric(uid)) {
                    uids.add(uid);
                }
            });
        }
        return String.join(",", uids);
    }
}
