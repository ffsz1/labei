package com.erban.admin.main.service.record;

import com.erban.admin.main.mapper.OutputValueMapperMgr;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.OutputValueParam;
import com.erban.admin.main.vo.OutputValueVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OutputValueService extends BaseService {
    @Autowired
    private OutputValueMapperMgr outputValueMapperMgr;

    public BusiResult getList(OutputValueParam outputValueParam){
        if(outputValueParam.getShowType() == null){
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        PageHelper.startPage(outputValueParam.getPageNum(), outputValueParam.getPageSize());
        outputValueParam.setSignBegin(outputValueParam.getSignBegin() + " 00:00:00");
        outputValueParam.setSignEnd(outputValueParam.getSignEnd() + " 23:59:59");
        List<OutputValueVo> outputValueVoList = outputValueMapperMgr.selectByParam(outputValueParam);
        if(outputValueVoList==null||outputValueVoList.size()==0){
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        String beginDate;
        Date endDate;
        long begin;
        BigDecimal num;
        BigDecimal sum;
        BigDecimal signNum=BigDecimal.ZERO;
        for(OutputValueVo outputValueVo:outputValueVoList) {
            begin = System.currentTimeMillis();
            beginDate = outputValueVo.getSignDate() + " 00:00:00";
            endDate = DateTimeUtil.getLastDay(DateTimeUtil.convertStrToDate(outputValueVo.getSignDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"), -13);
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
                }
            }
            System.out.println("耗时:" + (System.currentTimeMillis() - begin));
        }
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo<>(outputValueVoList));
    }

    /**
     * 统计登陆人
     * @param uidList 用户uid列表
     * @return
     */
    private BigDecimal getLoginRechargeSun(String uidList) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM account_login_record c force index(uid) WHERE c.uid in ("+uidList+") ", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

    /**
     * 统计登陆人数
     * @param uidList 用户uid列表
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    private List<Map<String,Object>> getLoginRechargeNum(String uidList, String beginDate, Date endDate) {
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(a.create_time, '%Y-%m-%d') as chargeDate,COUNT(DISTINCT a.uid) as num,TIMESTAMPDIFF(DAY,?,a.create_time) as differenceDate FROM account_login_record a force index(uid) WHERE a.uid in ("+uidList+") AND a.create_time BETWEEN ? and ? GROUP BY chargeDate", beginDate, beginDate, endDate);
    }

    private List<Map<String, Object>> getRechargeNum(String uidList, String beginDate, Date endDate) {
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(c.create_time, '%Y-%m-%d') as chargeDate, COUNT(DISTINCT c.uid) as num, TIMESTAMPDIFF(DAY,?,c.create_time) as differenceDate FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL AND c.create_time BETWEEN ? and ? GROUP BY chargeDate", beginDate, beginDate, endDate);
    }

    private List<Map<String, Object>> getRechargeAmount(String uidList, String beginDate, Date endDate) {
        return jdbcTemplate.queryForList("SELECT DATE_FORMAT(c.create_time, '%Y-%m-%d') as chargeDate, sum(c.amount)/100 as num, TIMESTAMPDIFF(DAY,?,c.create_time) as differenceDate FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL AND c.create_time BETWEEN ? and ? GROUP BY chargeDate", beginDate, beginDate, endDate);
    }

    private BigDecimal getRechargeSun(String uidList) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

    private BigDecimal getRechargeSun(String uidList, String beginDate, String endDate) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT c.uid) FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL AND c.create_time BETWEEN ? and ?", BigDecimal.class, beginDate, endDate);
        return result==null?BigDecimal.ZERO:result;
    }

    private BigDecimal getRechargeAmountSun(String uidList) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT sum(c.amount)/100 FROM charge_record c force index(uid) WHERE c.uid in ("+uidList+") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL", BigDecimal.class);
        return result==null?BigDecimal.ZERO:result;
    }

}
