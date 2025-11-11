package com.erban.admin.main.service.record;

import com.erban.admin.main.mapper.StatReportMapper;
import com.erban.admin.main.mapper.TreasureBoxMapperMgr;
import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.GiftVo;
import com.erban.admin.main.vo.TreasureBoxVo;
import com.erban.admin.main.vo.UsersVo;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.*;
import com.erban.main.param.admin.ChargeRecordParam;
import com.erban.main.param.admin.WithDrawParam;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.main.vo.admin.ChargeRecordVo;
import com.erban.main.vo.admin.DiamondWithDrawVo;
import com.erban.main.vo.admin.ExchangeDiamondVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountLoginRecordMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.vo.admin.AccountVo;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatReportAdminService extends BaseService {
    @Autowired
    private ChargeRecordMapper chargeRecordMapper;

    @Autowired
    private ExchangeDiamondGoldRecordMapper exchangeDiamondGoldRecordMapper;

    @Autowired
    private GiftSendRecordService giftSendRecordService;

    @Autowired
    private AccountLoginRecordMapper accountLoginRecordMapper;

    @Autowired
    private BillRecordMapper billRecordMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private StatReportMapper statReportMapper;

    /**
     * 整体报表
     *
     * @param
     * @return
     */
    public BusiResult getList(AccountParam accountParam) {
        PageHelper.startPage(accountParam.getPage(), accountParam.getSize());
        accountParam.setBeginDate(accountParam.getBeginDate() + " 00:00:00");
        accountParam.setEndDate(accountParam.getEndDate() + " 23:59:59");
        // accountParam.setSignBegin(accountParam.getSignBegin() + " 00:00:00");
        // accountParam.setSignEnd(accountParam.getSignEnd() + " 23:59:59");
        List<AccountVo> accountVoList = new ArrayList<>();
        List<Map<String, Object>> chargeAmount = new ArrayList<>();
        List<Map<String, Object>> exechangeDiamond = new ArrayList<>();
        List<Map<String, Object>> normalGiftNum = new ArrayList<>();
        List<Map<String, Object>> doCallNum = new ArrayList<>();
        List<Map<String, Object>> drawNum = new ArrayList<>();
        List<Map<String, Object>> diamondWithDraw = new ArrayList<>();
        List<Map<String, Object>> exper = new ArrayList<>();
        List<Map<String, Object>> charm = new ArrayList<>();

        if (!StringUtils.isBlank(accountParam.getErbanNos())) {
            String[] erbanNoArray = accountParam.getErbanNos().split("\n");
            List<Long> erbanList = new ArrayList<>();
            for (int i = 0; i < erbanNoArray.length; i++) {
                erbanList.add(Long.valueOf(erbanNoArray[i]));
            }
            accountParam.setErbanNosList(erbanList);
        }

        if (!StringUtils.isBlank(accountParam.getUserIds())) {
            String[] userIdArray = accountParam.getUserIds().split("\n");
            List<Long> userIds = new ArrayList<>();
            for (int i = 0; i < userIdArray.length; i++) {
                userIds.add(Long.valueOf(userIdArray[i]));
            }
            accountParam.setUids(userIds);
        }

        long begin = System.currentTimeMillis();
        if (accountParam.getOrderBy() == null) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        } else if (accountParam.getOrderBy().intValue() == 1) {
            accountVoList = statReportMapper.selectByChargeAmount(accountParam);
        } else if (accountParam.getOrderBy().intValue() == 2) {
            accountVoList = statReportMapper.selectByExechangeDiamond(accountParam);
        } else if (accountParam.getOrderBy().intValue() == 3) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
            // accountVoList = statReportMapper.selectByDiamond(accountParam);
        } else if (accountParam.getOrderBy().intValue() == 4) {
            accountVoList = statReportMapper.selectByExper(accountParam);
        } else if (accountParam.getOrderBy().intValue() == 5) {
            accountVoList = statReportMapper.selectByCharm(accountParam);
        }

        long end1 = System.currentTimeMillis();
        System.out.println("耗时:" + (end1 - begin));
        if (accountVoList == null || accountVoList.size() == 0) {
            return new BusiResult(BusiStatus.ADMIN_WITHDRAW_NOTEXIT);
        }

        // 归集UID
        List<Long> uids = new ArrayList<>(accountVoList.size());
        StringBuffer uidStr = new StringBuffer();
        for (AccountVo accountVo : accountVoList) {
            uids.add(accountVo.getUid());
            if (uidStr.length() == 0) {
                uidStr.append(accountVo.getUid());
            } else {
                uidStr.append(",").append(accountVo.getUid());
            }
        }

        if (accountParam.getOrderBy().intValue() == 1) {
            exechangeDiamond = getExechangeDiamond(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            normalGiftNum = getNormalGiftNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            doCallNum = getDoCallNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            drawNum = getDrawNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            // diamondWithDraw = getDiamondWithDraw(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exper = getChargeExper(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            charm = getChargeCharm(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
        } else if (accountParam.getOrderBy().intValue() == 2) {
            chargeAmount = getChargeAmount(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            normalGiftNum = getNormalGiftNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            doCallNum = getDoCallNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            drawNum = getDrawNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            // diamondWithDraw = getDiamondWithDraw(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exper = getChargeExper(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            charm = getChargeCharm(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
        } else if (accountParam.getOrderBy().intValue() == 3) {
            chargeAmount = getChargeAmount(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exechangeDiamond = getExechangeDiamond(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            normalGiftNum = getNormalGiftNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            doCallNum = getDoCallNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            drawNum = getDrawNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exper = getChargeExper(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            charm = getChargeCharm(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
        } else if (accountParam.getOrderBy().intValue() == 4) {
            chargeAmount = getChargeAmount(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exechangeDiamond = getExechangeDiamond(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            normalGiftNum = getNormalGiftNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            doCallNum = getDoCallNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            drawNum = getDrawNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            // diamondWithDraw = getDiamondWithDraw(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            charm = getChargeCharm(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
        } else if (accountParam.getOrderBy().intValue() == 5) {
            chargeAmount = getChargeAmount(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exechangeDiamond = getExechangeDiamond(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            normalGiftNum = getNormalGiftNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            doCallNum = getDoCallNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            drawNum = getDrawNum(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            // diamondWithDraw = getDiamondWithDraw(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
            exper = getChargeExper(uidStr.toString(), accountParam.getBeginDate(), accountParam.getEndDate());
        }

        long end2 = System.currentTimeMillis();
        System.out.println("耗时:" + (end2 - end1));
        Map<Long, Date> loginMap = getLatestLogin(uids);
        for (AccountVo accountVo : accountVoList) {
            Long uid = accountVo.getUid();
            for (Map<String, Object> map : chargeAmount) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 充值金额
                    accountVo.setChargeAmount(Long.valueOf(map.get("num").toString()) / 100);
                    break;
                }
            }

            for (Map<String, Object> map : exechangeDiamond) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 钻石兑换金币
                    accountVo.setExechangeDiamond(Double.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : normalGiftNum) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 送出普通礼物总值
                    accountVo.setNormalGiftNum(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : doCallNum) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 打Call总值
                    accountVo.setDoCallNum(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : drawNum) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 送海螺礼物总值
                    accountVo.setDrawNum(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : diamondWithDraw) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 钻石提现金额
                    accountVo.setDiamondWithDraw(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : exper) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 财富变化
                    accountVo.setExperChange(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            for (Map<String, Object> map : charm) {
                if (uid.toString().equals(map.get("uid").toString())) {
                    // 魅力变化
                    accountVo.setCharmChange(Long.valueOf(map.get("num").toString()));
                    break;
                }
            }

            // 财富值
            Long levelExer = giftSendRecordService.getLevelExerpence(uid);
            accountVo.setExperNum(levelExer);

            // 魅力值
            Long levelChar = giftSendRecordService.getLevelCharm(uid);
            accountVo.setCharmNum(levelChar);

            // 最近登录时间
            accountVo.setLastLoginTime(loginMap.get(uid) == null ? null : loginMap.get(uid));
        }

        long end3 = System.currentTimeMillis();
        System.out.println("耗时:" + (end3 - end2));
        return new BusiResult(BusiStatus.SUCCESS, new PageInfo(accountVoList));
    }

    /**
     * 查询用户在时间段内明细
     */
    public BusiResult getDetail(Long uid, Long erbanNo, String beginDate, String endDate, Integer type, Integer page,
                                Integer size) {
        if (type == 1) {
            //充值明细
            ChargeRecordParam chargeRecordParam = new ChargeRecordParam();
            chargeRecordParam.setErbanNo(erbanNo);
            chargeRecordParam.setBeginDate(beginDate + " 00:00:00");
            chargeRecordParam.setEndDate(endDate + " 23:59:59");
            PageHelper.startPage(page, size);
            List<ChargeRecordVo> chargeRecordVos = chargeRecordMapper.selectByQuery(chargeRecordParam);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo(chargeRecordVos));
        } else if (type == 2) {
            ChargeRecordParam chargeRecordParam = new ChargeRecordParam();
            chargeRecordParam.setErbanNo(erbanNo);
            chargeRecordParam.setBeginDate(beginDate + " 00:00:00");
            chargeRecordParam.setEndDate(endDate + " 23:59:59");
            //兑换金币
            PageHelper.startPage(page, size);
            List<ExchangeDiamondVo> exchangeDiamondVos =
                    exchangeDiamondGoldRecordMapper.selectByQuery(chargeRecordParam);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo(exchangeDiamondVos));
        } else if (type == 3) {
            //钻石提现
            WithDrawParam withDrawParam = new WithDrawParam();
            withDrawParam.setErbanNo(erbanNo);
            withDrawParam.setBeginDate(beginDate + " 00:00:00");
            withDrawParam.setEndDate(endDate + " 23:59:59");
            PageHelper.startPage(page, size);
            List<DiamondWithDrawVo> diamondWithDrawVos = billRecordMapper.selectByQuery(withDrawParam);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo(diamondWithDrawVos));
        } else if (type == 4) {
            Map<String, Object> map = new HashMap<>();
            map.put("sendUid", uid);
            map.put("beginDate", beginDate + " 00:00:00");
            map.put("endDate", endDate + " 23:59:59");
            //财富值
            PageHelper.startPage(page, size);
            List<GiftVo> giftVoList = statReportMapper.getGiftSend(map);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo(giftVoList));
        } else if (type == 5) {
            Map<String, Object> map = new HashMap<>();
            map.put("reciveUid", uid);
            map.put("beginDate", beginDate + " 00:00:00");
            map.put("endDate", endDate + " 23:59:59");
            //魅力值
            PageHelper.startPage(page, size);
            List<GiftVo> giftVoList = statReportMapper.getGiftSend(map);
            return new BusiResult(BusiStatus.SUCCESS, new PageInfo(giftVoList));
        }
        return new BusiResult(BusiStatus.BUSIERROR);
    }

    private Map<Long, Date> getLatestLogin(List<Long> uids) {
        Map<Long, Date> loginMap = new HashMap<>();
        List<AccountLoginRecord> loginRecords = accountLoginRecordMapper.getLatestLogin(uids);
        if (loginRecords == null) {
            return loginMap;
        }
        for (AccountLoginRecord accountLoginRecord : loginRecords) {
            loginMap.put(accountLoginRecord.getUid(), accountLoginRecord.getCreateTime());
        }
        return loginMap;
    }

    private List<Map<String, Object>> getChargeAmount(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT c.uid, sum(c.amount) as num FROM charge_record c force index(uid) " +
                "WHERE c.uid in (" + uids + ") and c.charge_status='2' AND c.pingxx_charge_id IS NOT NULL AND c" +
                ".create_time BETWEEN ? and ? GROUP BY c.uid", beginDate, endDate);
    }

    private List<Map<String, Object>> getExechangeDiamond(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT c.uid, sum(c.ex_diamond_num) as num FROM " +
                "exchange_diamond_gold_record c force index(uid) WHERE c.uid in (" + uids + ") AND c.create_time " +
                "BETWEEN ? and ? GROUP BY c.uid", beginDate, endDate);
    }

    private List<Map<String, Object>> getNormalGiftNum(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT b.giver_uid as uid, SUM(b.gift_num * g.gold_price) as num FROM " +
                "bill_gift_give b force index(uid) inner join gift g on g.gift_id = b.gift_id WHERE b.giver_uid in (" + uids + ") AND b.create_time " +
                "BETWEEN ? and ? GROUP BY b.giver_uid", beginDate, endDate);
    }

    private List<Map<String, Object>> getDoCallNum(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT b.giver_uid as uid, SUM(b.gift_num * g.gold_price) as num FROM " +
                "bill_gift_call b force index(uid) inner join gift g on g.gift_id = b.gift_id WHERE b.giver_uid in (" + uids + ") AND b.create_time " +
                "BETWEEN ? and ? GROUP BY b.giver_uid", beginDate, endDate);
    }

    private List<Map<String, Object>> getDrawNum(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT b.uid, SUM(b.gift_num * g.gold_price) as num FROM " +
                "bill_gift_draw b force index(uid) inner join gift g on g.gift_id = b.gift_id WHERE b.uid in (" + uids + ") AND b.create_time " +
                "BETWEEN ? and ? GROUP BY b.uid", beginDate, endDate);
    }

    // private List<Map<String, Object>> getDiamondWithDraw(String uids, String beginDate, String endDate) {
    //     return jdbcTemplate.queryForList("SELECT c.uid, sum(c.money) as num FROM bill_record c force index(key1)
    //             WHERE c.uid in("+uids+")AND c.obj_type = '2'AND c.create_time BETWEEN ? and ? GROUP BY c.uid", " +
    //                     "beginDate,
    //             endDate);
    // }

    private List<Map<String, Object>> getChargeExper(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT c.send_uid as uid, sum(c.sum_gold) as num FROM one_day_room_send_sum" +
                " c force index(key3) WHERE c.send_uid in (" + uids + ") AND c.create_time BETWEEN ? and ? GROUP BY c" +
                ".send_uid", beginDate, endDate);
    }

    private List<Map<String, Object>> getChargeCharm(String uids, String beginDate, String endDate) {
        return jdbcTemplate.queryForList("SELECT c.recv_uid as uid, sum(c.sum_gold) as num FROM one_day_room_recv_sum" +
                " c force index(key3) WHERE c.recv_uid in (" + uids + ") AND c.create_time BETWEEN ? and ? GROUP BY c" +
                ".recv_uid", beginDate, endDate);
    }
}
