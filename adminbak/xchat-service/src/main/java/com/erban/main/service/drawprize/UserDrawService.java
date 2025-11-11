package com.erban.main.service.drawprize;

import com.beust.jcommander.internal.Lists;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.UserDrawMapper;
import com.erban.main.mybatismapper.UserDrawMapperMgr;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.SysConfService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.main.vo.drawprize.UserDrawDoVo;
import com.erban.main.vo.drawprize.UserDrawRecordVo;
import com.erban.main.vo.drawprize.UserDrawVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserDrawService {
    @Autowired
    private UserDrawMapper userDrawMapper;
    @Autowired
    private UserDrawMapperMgr userDrawMapperMgr;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private UserDrawPrettyErbanNoService userDrawPrettyErbanNoService;
    @Autowired
    private UserDrawRecordService userDrawRecordService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;
    public static int sharePageId = 888;
    @Autowired
    private SysConfService sysConfService;
    private Gson gson = new Gson();
    private static DecimalFormat doubleFormat = new DecimalFormat("0.0000");

    public BusiResult getUserDrawByUid(Long uid) {
        UserDraw userDraw = getUserDrawBeanByUid(uid);
        UserDrawVo userDrawVo = convertUserDrawToVo(userDraw);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(userDrawVo);
        return busiResult;
    }

    public UserDraw getUserDrawBeanByUid(Long uid) {
        UserDraw userDraw = getUserDrawCache(uid);
        if (userDraw == null) {
            userDraw = querUserDraw(uid);
            if (userDraw == null) {
                userDraw = createUserDraw(uid, 0);
            }
        }
        return userDraw;
    }

    private static double genDoubleNumber() {
        Random ra = new Random();
        double dd = ra.nextDouble();
        double number = new Double(doubleFormat.format(dd));
        return number;
    }

    public void genUserDrawChanceByCharge(Long uid, Long amount, String objId) {
        String value = sysConfService.getSysConfValueById(Constant.SysConfId.draw_act_switch);
        if (!Constant.DrawSwitch.open.equals(value)) {//开关关闭，不生成抽奖机会
            return;
        }
        if(amount<800L){//少于8元的没有抽奖机会
            return;
        }
        UserDraw userDraw = getUserDrawBeanByUid(uid);
        String srcObjName = "充值" + amount / 100 + "元";
        updateUserDrawAndRecordByGen(userDraw, Constant.DrawRecordType.charge, amount, objId, srcObjName);
        sendPush(uid, convertUserDrawToVo(userDraw));
    }

    public void genUserDrawChanceByShare(Long uid, String objId) {
        String value = sysConfService.getSysConfValueById(Constant.SysConfId.draw_act_switch);
        if (!Constant.DrawSwitch.open.equals(value)) {//开关关闭，不生成抽奖机会
            return;
        }
        UserDraw userDraw = getUserDrawBeanByUid(uid);
        if (userDraw.getIsFirstShare()) {
            return;
        }
        userDraw.setIsFirstShare(true);
        String srcObjName = "首次分享";
        updateUserDrawAndRecordByGen(userDraw, Constant.DrawRecordType.share, null, objId, srcObjName);
        sendPush(uid, convertUserDrawToVo(userDraw));
    }

    private void sendPush(Long uid, UserDrawVo userDrawVo) {
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody("您获得了一次抽奖机会，快点去首页-幸运大抽奖抽奖吧！");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        // 目前由于不强制更新包，所以先发普通短信，到时再换回来
//        List<String> toAccids = Lists.newArrayList();
//        toAccids.add(uid.toString());
//        NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
//        neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
//        neteaseSendMsgBatchParam.setToAccids(toAccids);
//        neteaseSendMsgBatchParam.setType(100);
//        neteaseSendMsgBatchParam.setPushcontent("您获得了一次抽奖机会，快去拿奖吧！");
//        Body body = new Body();
//        body.setFirst(Constant.DefMsgType.Draw);
//        body.setSecond(Constant.DefMsgType.DrawChance);
//        body.setData(userDrawVo);
//        neteaseSendMsgBatchParam.setBody(body);
//        Payload payload = new Payload();
//        neteaseSendMsgBatchParam.setPayload(payload);
//        sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
    }

    public UserDraw createUserDraw(Long uid, int leftDrawNum) {
        Date date = new Date();
        UserDraw userDraw = new UserDraw();
        userDraw.setUid(uid);
        userDraw.setLeftDrawNum(leftDrawNum);
        userDraw.setTotalDrawNum(leftDrawNum);
        userDraw.setIsFirstShare(false);
        userDraw.setTotalWinDrawNum(0);
        userDraw.setCreateTime(date);
        userDraw.setUpdateTime(date);
        userDrawMapper.insert(userDraw);
        saveUserDrawCache(userDraw);
        return userDraw;
    }

    private UserDrawVo convertUserDrawToVo(UserDraw userDraw) {
        UserDrawVo userDrawVo = new UserDrawVo();
        if (userDraw == null) {
            return userDrawVo;
        }
        userDrawVo.setUid(userDraw.getUid());
        userDrawVo.setTotalDrawNum(userDraw.getTotalDrawNum());
        userDrawVo.setTotalWinDrawNum(userDraw.getTotalWinDrawNum());
        userDrawVo.setLeftDrawNum(userDraw.getLeftDrawNum());
        return userDrawVo;
    }

    private UserDraw querUserDraw(Long uid) {
        UserDraw userDraw = userDrawMapper.selectByPrimaryKey(uid);
        return userDraw;
    }

    private void saveUserDrawCache(UserDraw userDraw) {
        if (userDraw == null) {
            return;
        }
        Long uid = userDraw.getUid();
        jedisService.hwrite(RedisKey.user_draw.getKey(), uid.toString(), gson.toJson(userDraw));
    }

    private UserDraw getUserDrawCache(Long uid) {
        String userDrawStr = jedisService.hget(RedisKey.user_draw.getKey(), uid.toString());
        if (StringUtils.isEmpty(userDrawStr)) {
            return null;
        }
        UserDraw userDraw = gson.fromJson(userDrawStr, UserDraw.class);
        return userDraw;
    }

    public BusiResult<UserDrawDoVo> doUserDraw(Long uid) {
        UserDraw userDraw = getUserDrawBeanByUid(uid);
        if (userDraw.getLeftDrawNum() == 0) {
            return new BusiResult<>(BusiStatus.NOMORECHANCE);
        }
        UserDrawRecord userDrawRecord = userDrawRecordService.getUserDrawRecordByUidOfCreate(uid);
        if (userDrawRecord == null) {
            return new BusiResult<>(BusiStatus.NOMORECHANCE);
        }

        userDrawRecord = getDrawPrizeProd(userDrawRecord);
        userDraw = updateUserDrawByDoDraw(userDraw, userDrawRecord);
        UserDrawDoVo userDrawDoVo = convertUserDrawDoToVo(userDraw, userDrawRecord);
        BusiResult<UserDrawDoVo> busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(userDrawDoVo);
        return busiResult;
    }

    private UserDraw updateUserDrawDb(UserDraw userDraw) {
        userDraw.setUpdateTime(new Date());
        userDrawMapper.updateByPrimaryKeySelective(userDraw);
        return userDraw;
    }

    /**
     * 更新抽奖记录
     *
     * @param userDraw
     * @param userDrawRecord
     * @return
     */
    private UserDraw updateUserDrawByDoDraw(UserDraw userDraw, UserDrawRecord userDrawRecord) {
        int leftDrawNum = userDraw.getLeftDrawNum();
        leftDrawNum--;
        userDraw.setLeftDrawNum(leftDrawNum);
        int totalWinDrawNum = userDraw.getTotalWinDrawNum();
        if (Constant.DrawStatus.hasPrize.equals(userDrawRecord.getDrawStatus())) {//判断有没有中奖，如果有中奖，判断是加金币还是靓号
            totalWinDrawNum++;
            if (userDrawRecord.getDrawPrizeId() <= Constant.DrawProd.gold8888) {//金币充值，增加金币账单
                doUserPurseAndBillRecord(userDraw.getUid(), Long.valueOf(userDrawRecord.getDrawPrizeId()), userDrawRecord.getRecordId().toString());
            }else if(userDrawRecord.getDrawPrizeId() <= Constant.DrawProd.prettySeven){//修改抽奖靓号的状态
                userDrawPrettyErbanNoService.updatePrettyErbanNoByType(new Byte("1"));
            }
        }
        userDraw.setTotalWinDrawNum(totalWinDrawNum);
        updateUserDraw(userDraw);
        userDrawRecordService.updateUserDrawRrecord(userDrawRecord);
        return userDraw;
    }

    private void doUserPurseAndBillRecord(Long uid, Long goldNum, String objId) {
        userPurseUpdateService.addGoldDbAndCache(uid, goldNum);
        billRecordService.insertBillRecord(uid, null, objId, Constant.BillType.draw, null, goldNum, 0L);

    }

    private UserDraw updateUserDrawAndRecordByGen(UserDraw userDraw, Byte type, Long amount, String srcObjId, String srcObjName) {
        int leftDrawNum = userDraw.getLeftDrawNum();
        leftDrawNum++;
        userDraw.setLeftDrawNum(leftDrawNum);
        int totalDrawNum = userDraw.getTotalDrawNum();
        totalDrawNum++;
        userDraw.setTotalDrawNum(totalDrawNum);
        updateUserDraw(userDraw);

        Long uid = userDraw.getUid();

        userDrawRecordService.genUserDrawRecord(uid, type, amount, srcObjId, srcObjName);
        return userDraw;
    }

    private UserDraw updateUserDraw(UserDraw userDraw) {
        updateUserDrawDb(userDraw);
        saveUserDrawCache(userDraw);
        return userDraw;
    }

    /**
     * 获取已经中奖的记录20条，用于滚屏
     *
     * @return
     */
    public BusiResult<List<UserDrawRecordVo>> getUserDrawRecordListByStatus() {
        List<UserDrawRecord> userDrawRecordList = userDrawMapperMgr.getUserDrawRecordListByStatus();
        List<UserDrawRecordVo> userDrawRecordVoList = convertUserDrawRecordToVo(userDrawRecordList);
        BusiResult busResuslt = new BusiResult(BusiStatus.SUCCESS);
        busResuslt.setData(userDrawRecordVoList);
        return busResuslt;
    }

    private List<UserDrawRecordVo> convertUserDrawRecordToVo(List<UserDrawRecord> userDrawRecordList) {
        if (CollectionUtils.isEmpty(userDrawRecordList)) {
            return Lists.newArrayList();
        }

        List<UserDrawRecordVo> userDrawRecordVoList = Lists.newArrayList();
        for (UserDrawRecord userDrawRecord : userDrawRecordList) {
            UserDrawRecordVo userDrawRecordVo = new UserDrawRecordVo();
            Long uid = userDrawRecord.getUid();
            UserVo userVo = usersService.getUserVoByUid(uid);
            userDrawRecordVo.setUserVo(userVo);
            userDrawRecordVo.setDrawPrizeName(userDrawRecord.getDrawPrizeName());
            userDrawRecordVo.setType(userDrawRecord.getType());
            userDrawRecordVo.setDrawStatus(userDrawRecord.getDrawStatus());
            userDrawRecordVo.setSrcObjName(userDrawRecord.getSrcObjName());
            userDrawRecordVoList.add(userDrawRecordVo);
        }
        return userDrawRecordVoList;
    }

    private UserDrawDoVo convertUserDrawDoToVo(UserDraw userDraw, UserDrawRecord userDrawRecord) {
        if (userDraw == null) {
            return new UserDrawDoVo();
        }
        UserDrawDoVo userDrawDoVo = new UserDrawDoVo();
        userDrawDoVo.setTotalDrawNum(userDraw.getTotalDrawNum());
        userDrawDoVo.setLeftDrawNum(userDraw.getLeftDrawNum());
        userDrawDoVo.setDrawStatus(userDrawRecord.getDrawStatus());
        userDrawDoVo.setDrawPrizeName(userDrawRecord.getDrawPrizeName());
        userDrawDoVo.setSrcObjName(userDrawRecord.getSrcObjName());
        userDrawDoVo.setTotalWinDrawNum(userDraw.getTotalWinDrawNum());
        userDrawDoVo.setDrawPrizeId(userDrawRecord.getDrawPrizeId());
        userDrawDoVo.setUserVo(usersService.getUserVoByUid(userDraw.getUid()));
        return userDrawDoVo;
    }

    private UserDrawRecord getDrawPrizeProd(UserDrawRecord userDrawRecord) {
        Integer amount = userDrawRecord.getSrcObjAmount().intValue();
        double drawPrize[];
        double randomNumber = genDoubleNumber();
        if (amount.equals(4800)) {
            drawPrize= Constant.DrawRange.charge48;//中奖机会：不中/8/50金币
            if(randomNumber>=drawPrize[0]&&randomNumber<=drawPrize[1]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.none);
                userDrawRecord.setDrawPrizeName("谢谢参与，继续努力！");
                userDrawRecord.setDrawStatus(Constant.DrawStatus.nonePrize);
            }else if(randomNumber>drawPrize[1]&&randomNumber<=drawPrize[2]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8+"金币");
            }else if(randomNumber>drawPrize[2]&&randomNumber<=drawPrize[3]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold50);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold50+"金币");
            }
        }else if (amount.equals(9800)) {
            drawPrize = Constant.DrawRange.charge98;//中奖机会：不中/8/50/100金币
            if(randomNumber>=drawPrize[0]&&randomNumber<=drawPrize[1]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.none);
                userDrawRecord.setDrawPrizeName("谢谢参与，继续努力！");
                userDrawRecord.setDrawStatus(Constant.DrawStatus.nonePrize);
            }else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold50);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold50 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold100);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold100 + "金币");
            }
        } else if (amount.equals(19800)) {//中奖机会：8/50/100/300金币
            drawPrize = Constant.DrawRange.charge198;
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold50);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold50 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold100);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold100 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold300);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold300 + "金币");
            }
        } else if (amount.equals(49800)) {
            drawPrize = Constant.DrawRange.charge498;//中奖机会：50/100/300/1000金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold50);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold50 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold100);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold100 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold300);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold300 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold1000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold1000 + "金币");
            }
        } else if (amount.equals(99800)) {
            drawPrize = Constant.DrawRange.charge998;//中奖机会：100/300/1000/3000金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold100);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold100 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold300);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold300 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold1000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold1000 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold3000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold3000 + "金币");
            }
        } else if (amount.equals(499800)) {
            drawPrize = Constant.DrawRange.charge4998;//中奖机会：300/1000/3000/8888/10000(靓号)金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold300);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold300 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold1000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold1000 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold3000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold3000 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8888);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8888 + "金币");
            } else if (randomNumber > drawPrize[4] && randomNumber <= drawPrize[5]) {
                UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoService.getNotUsePrettyErbanNoByType(Constant.DrawPrettyErbanNoType.seven);
                userDrawRecord.setDrawPrizeName("七位靓号" + userDrawPrettyErbanNo.getPrettyErbanNo());
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.prettySeven);
            }
        } else if (amount.equals(999900)) {
            drawPrize = Constant.DrawRange.charge9999;//中奖机会：1000/3000/8888/10000(靓号)金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold1000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold1000 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold3000);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold3000 + "金币");
            } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8888);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8888 + "金币");
            } else if (randomNumber > drawPrize[3] && randomNumber <= drawPrize[4]) {
                UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoService.getNotUsePrettyErbanNoByType(Constant.DrawPrettyErbanNoType.seven);
                userDrawRecord.setDrawPrizeName("七位靓号" + userDrawPrettyErbanNo.getPrettyErbanNo());
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.prettySeven);
            }
        } else if (amount.equals(3000000)) {
            drawPrize = Constant.DrawRange.charge30000;//中奖机会：8888/10000(靓号)金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8888);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8888 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoService.getNotUsePrettyErbanNoByType(Constant.DrawPrettyErbanNoType.seven);
                userDrawRecord.setDrawPrizeName("七位靓号" + userDrawPrettyErbanNo.getPrettyErbanNo());
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.prettySeven);
            }
        } else if (amount.equals(6000000)) {
            drawPrize = Constant.DrawRange.charge60000;//中奖机会：8888/10000(靓号)金币
            if (randomNumber >= drawPrize[0] && randomNumber <= drawPrize[1]) {
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8888);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8888 + "金币");
            } else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
                UserDrawPrettyErbanNo userDrawPrettyErbanNo = userDrawPrettyErbanNoService.getNotUsePrettyErbanNoByType(Constant.DrawPrettyErbanNoType.seven);
                userDrawRecord.setDrawPrizeName("七位靓号" + userDrawPrettyErbanNo.getPrettyErbanNo());
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.prettySeven);
            }
        }else {// 800
            drawPrize= Constant.DrawRange.charge8;//中奖机会：不中/8/50金币
            if(randomNumber>=drawPrize[0]&&randomNumber<=drawPrize[1]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.none);
                userDrawRecord.setDrawPrizeName("谢谢参与，继续努力！");
                userDrawRecord.setDrawStatus(Constant.DrawStatus.nonePrize);
            }else if(randomNumber>drawPrize[1]&&randomNumber<=drawPrize[2]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold8);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold8+"金币");
            }else if(randomNumber>drawPrize[2]&&randomNumber<=drawPrize[3]){
                userDrawRecord.setDrawPrizeId(Constant.DrawProd.gold50);
                userDrawRecord.setDrawPrizeName(Constant.DrawProd.gold50+"金币");
            }
        }
        if (!Constant.DrawStatus.nonePrize.equals(userDrawRecord.getDrawStatus())) {
            userDrawRecord.setDrawStatus(Constant.DrawStatus.hasPrize);
        }
        userDrawRecord.setUpdateTime(new Date());
        return userDrawRecord;
    }

}
