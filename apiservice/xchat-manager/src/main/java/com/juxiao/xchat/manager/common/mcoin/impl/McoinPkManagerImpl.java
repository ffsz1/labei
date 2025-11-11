package com.juxiao.xchat.manager.common.mcoin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.McoinPkInfoDao;
import com.juxiao.xchat.dao.mcoin.McoinUserPkInfoDao;
import com.juxiao.xchat.dao.mcoin.domain.McoinUserPkInfoDO;
import com.juxiao.xchat.dao.mcoin.dto.McoinPkInfoDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinUserPkInfoDTO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.mcoin.McoinPkManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseBatchMsgBO;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class McoinPkManagerImpl implements McoinPkManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private McoinPkInfoDao mcoinPkInfoDao;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private McoinUserPkInfoDao mcoinUserPkInfoDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private Gson gson;
    @Autowired
    private NetEaseMsgManager neteaseMsgManager;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AsyncNetEaseTrigger msgPushManager;

    private final String MCOIN_PK_LOCK = "mcoin_pk_lock";

    /**
     * 获取正在进行的点点币PK活动信息
     * @return
     * @throws WebServiceException
     */
    @Override
    public JSONObject getPkInfo(Long uid) throws WebServiceException {

        //根据活动状态获取到状态正在进行的活动信息
        McoinPkInfoDTO mcoinPkInfoDTO = mcoinPkInfoDao.getForStatusNormal((byte)1);
        //判断活动是否正常进行状态
        if (null == mcoinPkInfoDTO){
            throw new WebServiceException(WebServiceCode.MCOIN_PK_STATUS_ERR);
        }

        //获取本期的期号
        int item = mcoinPkInfoDTO.getTerm();
        String termStr = redisManager.get(RedisKey.mcoin_pk_this_term + "");
        int term = termStr == null ? 0 : Integer.valueOf(termStr);
        if (item != term){
            redisManager.set(RedisKey.mcoin_pk_this_term + "",Integer.toString(item));
            //删除上一期的排行榜数据
            deleteRedis(term,uid);
            //趁机更新排行榜信息
            updateRankingUserData(item,uid);
            updateRankingData(item);
            //更新往期回顾信息
            updatePastPeriod(item);
        }

        //获取红方票数、蓝方票数、可瓜分点点币数量缓存数据
        String pkRedPolls = redisManager.hget(RedisKey.mcoin_pk_support_red_polls + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"");
        int redPolls = pkRedPolls == null ? 0 : Integer.valueOf(pkRedPolls);
        String pkBluePolls = redisManager.hget(RedisKey.mcoin_pk_support_blue_polls + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"");
        int bluePolls = pkBluePolls == null ? 0 : Integer.valueOf(pkBluePolls);
        String pkCarveUpNum = redisManager.hget(RedisKey.mcoin_pk_carve_up_number + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"");
        int carveUpNum = pkCarveUpNum == null ? 0 : Integer.valueOf(pkCarveUpNum);

        //获取信息，存入JSONObject中
        JSONObject obj = new JSONObject();

        obj.put("term",item);//期数
        obj.put("title",mcoinPkInfoDTO.getTitle());//题目
        obj.put("redAnswer",mcoinPkInfoDTO.getRedAnswer());//红方答案
        obj.put("blueAnswer",mcoinPkInfoDTO.getBlueAnswer());//蓝方答案
        obj.put("redPolls",formatNum(redPolls));//红方票数
        obj.put("bluePolls",formatNum(bluePolls));//蓝方票数
        obj.put("redPic",mcoinPkInfoDTO.getRedPic());//红方头像
        obj.put("bluePic",mcoinPkInfoDTO.getBluePic());//蓝方头像
        obj.put("lotteryTime",mcoinPkInfoDTO.getLotteryTime());//开奖时间
        obj.put("carveUpMcoinNum",carveUpNum);//可瓜分的萌币数量

        return obj;
    }


    /**
     * 点击支持PK
     * @param uid
     * @param supportType 1为red队 2为blue队
     * @throws WebServiceException
     */
    @Override
    public void supportPk(Long uid, Integer supportType) throws WebServiceException {

        if (uid == null || supportType == null) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        //根据活动状态获取到状态正在进行的活动信息
        McoinPkInfoDTO mcoinPkInfoDTO = mcoinPkInfoDao.getForStatusNormal((byte)1);
        //判断活动是否正常进行状态
        if (null == mcoinPkInfoDTO){
            throw new WebServiceException(WebServiceCode.MCOIN_PK_STATUS_ERR);
        }
        String scratchLockKey = MCOIN_PK_LOCK+"_"+uid;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            //查询用户萌币账户是否足够抵扣此次PK
            UserMcoinPurseDTO mcoinPurseDTO = mcoinManager.getUserMcoinPurse(uid);
            if (mcoinPurseDTO.getMcoinNum() < 10){
                throw new WebServiceException(WebServiceCode.MCOIN_PURSE_NOT_ENOUGH);
            }
            //查询活动开奖时间相差是否小于30s
            if ( mcoinPkInfoDTO.getLotteryTime().getTime() - System.currentTimeMillis() <= 30000){
                throw new WebServiceException(WebServiceCode.MCOIN_PK_STATUS_ERR);
            }
            //扣除10萌币
            mcoinManager.updateReduceMcoin(uid,10);
            //可瓜分萌币数+10
            redisManager.hincrBy(RedisKey.mcoin_pk_carve_up_number + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"",10L);
            //支持成功，相应在该队缓存+1
            if (supportType == 1){//红队
                redisManager.hincrBy(RedisKey.mcoin_user_pk_support_red_polls + "_" + mcoinPkInfoDTO.getTerm(), uid+"",1L);
                redisManager.hincrBy(RedisKey.mcoin_pk_support_red_polls + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"",1L);
            }else if (supportType == 2){//蓝队
                redisManager.hincrBy(RedisKey.mcoin_user_pk_support_blue_polls + "_" + mcoinPkInfoDTO.getTerm(), uid+"",1L);
                redisManager.hincrBy(RedisKey.mcoin_pk_support_blue_polls + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm()+"",1L);
            }
            //获取支持红方票数、支持蓝方票数
            String pkRedPolls = redisManager.hget(RedisKey.mcoin_user_pk_support_red_polls + "_" + mcoinPkInfoDTO.getTerm(), uid+"");
            int redPolls = pkRedPolls == null ? 0 : Integer.valueOf(pkRedPolls);
            String pkBluePolls = redisManager.hget(RedisKey.mcoin_user_pk_support_blue_polls + "_" + mcoinPkInfoDTO.getTerm(), uid+"");
            int bluePolls = pkBluePolls == null ? 0 : Integer.valueOf(pkBluePolls);
            //查询该用户有没有此次PK活动数据
            McoinUserPkInfoDTO userPkInfoDTO = mcoinUserPkInfoDao.getForTermAndUid(mcoinPkInfoDTO.getTerm(),uid);
            if (null == userPkInfoDTO){
                //没有则在数据库中创建一条数据
                McoinUserPkInfoDO upiDO = new McoinUserPkInfoDO();
                upiDO.setUid(uid);
                upiDO.setTerm(mcoinPkInfoDTO.getTerm());
                upiDO.setRedPolls(redPolls);
                upiDO.setBluePolls(bluePolls);
                upiDO.setPayMcoinNumber(10);
                upiDO.setCarveUpMcoinNum(0);
                upiDO.setArrivalMcoin((byte)0);
                upiDO.setCreateTime(new Date());
                mcoinUserPkInfoDao.save(upiDO);
            }

        } finally {
            redisManager.unlock(scratchLockKey, lockVal);
        }
    }

    //开奖
    @Override
    public void lottery(int term,Long endTime) throws WebServiceException {
        redisManager.hdel(RedisKey.mcoin_pk_lottery_tag + "_" + term, term+"");
        //根据活动状态获取到状态正在进行的活动信息
        McoinPkInfoDTO mcoinPkInfoDTO = mcoinPkInfoDao.getByTerm(term);
        //判断活动是否正常进行状态
        if (null == mcoinPkInfoDTO) {
            return;
        }

        String scratchLockKey = MCOIN_PK_LOCK + "_" + term;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            /*1、拿到本期的数据，算出多数派每票能获得的萌币数量=瓜分总数/多数派票数；*/
            //获取红方票数、蓝方票数、可瓜分萌币数量缓存数据
            String pkRedPolls = redisManager.hget(RedisKey.mcoin_pk_support_red_polls + "_" + term, term + "");
            int redPolls = pkRedPolls == null ? 0 : Integer.valueOf(pkRedPolls);
            String pkBluePolls = redisManager.hget(RedisKey.mcoin_pk_support_blue_polls + "_" + term, term + "");
            int bluePolls = pkBluePolls == null ? 0 : Integer.valueOf(pkBluePolls);
            String pkCarveUpNum = redisManager.hget(RedisKey.mcoin_pk_carve_up_number + "_" + term, term + "");
            int carveUpNum = pkCarveUpNum == null ? 0 : Integer.valueOf(pkCarveUpNum);

            //TODO 更新数据到数据库，并置状态为已经结束，记录更新时间
            mcoinPkInfoDao.updatePoolsAndNum(redPolls, bluePolls, carveUpNum, mcoinPkInfoDTO.getId(),term);
            redisManager.hdel(RedisKey.mcoin_pk_send_mq_message_status + "",mcoinPkInfoDTO.getId().toString());
            if (redPolls == 0 && bluePolls == 0){
                //下一期开始
                Integer nextTerm = mcoinPkInfoDao.findByNextTerm();
                if (null == nextTerm || nextTerm.equals("")){
                    return ;
                }
                logger.info("现在时间{},第{}期点点币Pk活动开始",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),nextTerm);
                //发全服消息
                this.pkBeginning(nextTerm);
                return;
            }

            //记录哪个队是多数派（1代表red 2代表blue）
            int flag = 1;
            //每票能瓜分的萌币数量
            int avgNum = 0;
            if (redPolls < bluePolls) {//蓝队瓜分
                avgNum = carveUpNum / bluePolls;
                flag = 2;
            } else if (redPolls > bluePolls) {//红队瓜分
                avgNum = carveUpNum / redPolls;
            } else {//随机抽一个
                int carveBlueRe = bluePolls == 0 ? 0 : carveUpNum / bluePolls;
                int carveRedRe = redPolls == 0 ? 0 : carveUpNum / redPolls;
                avgNum = System.currentTimeMillis() % 2 == 0 ? carveBlueRe : carveRedRe;
                flag = System.currentTimeMillis() % 2 == 0 ? 2 : 1;
            }
            /*2、拿到本期参与活动的用户，算出该用户能瓜分的萌币数量=支持多数派的票数*每票能获得的萌币数量，累加到该用户萌币账户中。*/
            List<McoinUserPkInfoDTO> list = mcoinUserPkInfoDao.getForTerm(term);
            if (!list.isEmpty()) {
                //saveUserPkData(list,term,flag,avgNum);
                saveUserPkData(list,term,flag,carveUpNum,redPolls,bluePolls);
            }
            //下一期开始
            Integer nextTerm = mcoinPkInfoDao.findByNextTerm();
            if (null == nextTerm || nextTerm.equals("")){
                return ;
            }
            logger.info("现在时间{},第{}期点点币Pk活动开始",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),nextTerm);
            //发全服消息
            this.pkBeginning(nextTerm);

        } catch (Exception e) {
            logger.error("第{}期点点币Pk活动开奖报错：",term,e);
        } finally {
            redisManager.unlock(scratchLockKey, lockVal);
        }
    }

    /**
     * 统计保存用户活动奖励
     * 用户瓜分萌币数  =   奖池*自己投入/胜方萌币数 即carveUpNum*（用户胜方投票数*10）/胜方票数*10
     * @param list                  用户列表
     * @param term                  期数
     * @param flag                  记录哪个队是多数派（1代表red 2代表blue）
     * @param carveUpNum           总奖池
     * @param redPolls             红队票数
     * @param bluePolls            蓝队票数
     * @throws WebServiceException
     */
    public void saveUserPkData(List<McoinUserPkInfoDTO> list,int term,int flag,int carveUpNum,int redPolls,int bluePolls) throws WebServiceException {

        BigDecimal gfNum = new BigDecimal(0);//能瓜分的萌币数量
        for (McoinUserPkInfoDTO dto : list) {
            String pkUserRedPolls = redisManager.hget(RedisKey.mcoin_user_pk_support_red_polls + "_" + term, dto.getUid() + "");
            int redUserPolls = pkUserRedPolls == null ? 0 : Integer.valueOf(pkUserRedPolls);
            String pkUserBluePolls = redisManager.hget(RedisKey.mcoin_user_pk_support_blue_polls + "_" + term, dto.getUid() + "");
            int blueUserPolls = pkUserBluePolls == null ? 0 : Integer.valueOf(pkUserBluePolls);
            //算出该用户能瓜分的萌币数量 = 奖池*自己投入/胜方萌币数 即carveUpNum*（用户胜方投票数*10）/胜方票数*10
            if (flag == 1) {//红队瓜分
                BigDecimal a = new BigDecimal(carveUpNum * redUserPolls * 10);
                BigDecimal b = new BigDecimal(redPolls * 10);
                gfNum = a.divide(b,BigDecimal.ROUND_HALF_UP);
            } else {//蓝队瓜分
                BigDecimal a = new BigDecimal(carveUpNum * blueUserPolls * 10);
                BigDecimal b = new BigDecimal(bluePolls * 10);
                gfNum = a.divide(b,BigDecimal.ROUND_HALF_UP);
            }

            byte arrivalMcoin = 1;
            if (gfNum.compareTo(new BigDecimal(0)) == 0) {
                arrivalMcoin = 0;
            }
            mcoinUserPkInfoDao.updatePollsAndMcoin(redUserPolls, blueUserPolls, (redUserPolls + blueUserPolls) * 10, gfNum.intValue(), arrivalMcoin, dto.getUid(), dto.getTerm());
            //推送
            NeteaseMsgBO msgBo = new NeteaseMsgBO();
            msgBo.setFrom(systemConf.getSecretaryUid());
            msgBo.setTo(String.valueOf(dto.getUid()));
            msgBo.setOpe(0);
            msgBo.setType(0);
            msgBo.setBody("恭喜你成功瓜分点点币PK第" + dto.getTerm() + "期的奖池，获得" + gfNum + "点点币！");
            if (gfNum.compareTo(new BigDecimal(0)) != 0){
                neteaseMsgManager.sendMsg(msgBo);
            }

            //TODO 萌币存入用户账户中，保存数据到数据库，更新萌币到账状态。
            mcoinManager.updateAddMcoin(dto.getUid(), gfNum.intValue());
            //删除redis
            redisManager.hdel(RedisKey.mcoin_user_pk_support_red_polls + "_" + term, dto.getUid()+"");
            redisManager.hdel(RedisKey.mcoin_user_pk_support_blue_polls + "_" + term, dto.getUid()+"");
            redisManager.hdel(RedisKey.mcoin_pk_support_red_polls + "_" + term, term+"");
            redisManager.hdel(RedisKey.mcoin_pk_support_blue_polls + "_" + term, term+"");
            redisManager.hdel(RedisKey.mcoin_pk_carve_up_number + "_" + term, term+"");
        }
    }
    /**
     * 保存用户萌币PK活动数据
     * @param list
     * @param term
     * @param flag
     * @param avgNum
     * @throws WebServiceException
     */
    public void saveUserPkData2(List<McoinUserPkInfoDTO> list,int term,int flag,int avgNum) throws WebServiceException {

        int gfNum = 0;//能瓜分的萌币数量
        for (McoinUserPkInfoDTO dto : list) {
            String pkUserRedPolls = redisManager.hget(RedisKey.mcoin_user_pk_support_red_polls + "_" + term, dto.getUid() + "");
            int redUserPolls = pkUserRedPolls == null ? 0 : Integer.valueOf(pkUserRedPolls);
            String pkUserBluePolls = redisManager.hget(RedisKey.mcoin_user_pk_support_blue_polls + "_" + term, dto.getUid() + "");
            int blueUserPolls = pkUserBluePolls == null ? 0 : Integer.valueOf(pkUserBluePolls);
            //算出该用户能瓜分的萌币数量=支持多数派的票数*每票能获得的萌币数量
            if (flag == 1) {//红队瓜分
                gfNum = redUserPolls * avgNum;
            } else {//蓝队瓜分
                gfNum = blueUserPolls * avgNum;
            }

            byte arrivalMcoin = 1;
            if (gfNum == 0) {
                arrivalMcoin = 0;
            }
            mcoinUserPkInfoDao.updatePollsAndMcoin(redUserPolls, blueUserPolls, (redUserPolls + blueUserPolls) * 10, gfNum, arrivalMcoin, dto.getUid(), dto.getTerm());
            //推送
            NeteaseMsgBO msgBo = new NeteaseMsgBO();
            msgBo.setFrom(systemConf.getSecretaryUid());
            msgBo.setTo(String.valueOf(dto.getUid()));
            msgBo.setOpe(0);
            msgBo.setType(0);
            msgBo.setBody("恭喜你成功瓜分点点币PK第" + dto.getTerm() + "期的奖池，获得" + gfNum + "点点币！");
            if (gfNum != 0){
                neteaseMsgManager.sendMsg(msgBo);
            }

            //TODO 萌币存入用户账户中，保存数据到数据库，更新萌币到账状态。
            mcoinManager.updateAddMcoin(dto.getUid(), gfNum);
            //删除redis
            redisManager.hdel(RedisKey.mcoin_user_pk_support_red_polls + "_" + term, dto.getUid()+"");
            redisManager.hdel(RedisKey.mcoin_user_pk_support_blue_polls + "_" + term, dto.getUid()+"");
            redisManager.hdel(RedisKey.mcoin_pk_support_red_polls + "_" + term, term+"");
            redisManager.hdel(RedisKey.mcoin_pk_support_blue_polls + "_" + term, term+"");
            redisManager.hdel(RedisKey.mcoin_pk_carve_up_number + "_" + term, term+"");
        }
    }

    @Override
    public JSONObject rankingList(Long uid) throws WebServiceException {

        JSONObject object = new JSONObject();
        //获取本期期号
        String termStr = redisManager.get(RedisKey.mcoin_pk_this_term + "");
        int term = termStr == null ? 0 : Integer.valueOf(termStr);
        //获取个人数据
        //累计赢得萌币数（不含本次）
        String historyGainStr = redisManager.hget(RedisKey.mcoin_pk_history_gain + "_" + term, Long.toString(uid));
        int historyGain = historyGainStr == null ? 0 : Integer.valueOf(historyGainStr);
        //参与场次（不含本次）
        String historyTimesStr = redisManager.hget(RedisKey.mcoin_pk_history_times + "_" + term, Long.toString(uid));
        int historyTimes = historyTimesStr == null ? 0 : Integer.valueOf(historyTimesStr);
        //历史获胜场次(不含本次)
        String historyWinTimesStr = redisManager.hget(RedisKey.mcoin_pk_history_win_times + "_" + term, Long.toString(uid));
        int historyWinTimes = historyWinTimesStr == null ? 0 : Integer.valueOf(historyWinTimesStr);
        //胜率（不含本次）
        String historyWinRateStr = redisManager.hget(RedisKey.mcoin_pk_history_win_rate + "_" + term, Long.toString(uid));
        float historyWinRate = historyWinRateStr == null ? 0 : Float.valueOf(historyWinRateStr);
        //个人数据
        if (historyGain == 0 && historyTimes == 0 && historyWinTimes == 0 && historyWinRate == 0f){
            //更新个人数据
            updateRankingUserData(term,uid);
        }
        object.put("avatar",usersManager.getUser(uid).getAvatar());
        object.put("historyGain",historyGain);
        object.put("historyTimes",historyTimes);
        object.put("historyWinTimes",historyWinTimes);
        object.put("historyWinRate",historyWinRate);
        //pk榜单
        Set<String> rankingSet = redisManager.zrangeByScore(RedisKey.mcoin_pk_history_ranking_list + "_" + term, 0, System.currentTimeMillis() / 1000);
        if (null == rankingSet || rankingSet.isEmpty()) {
            updateRankingData(term);
        }
        List<PkRanking> rankingList = new ArrayList<>();
        for (String str : rankingSet){
            PkRanking pkRanking = (PkRanking) JSON.parseObject(str, PkRanking.class);
            //PkRanking pkRanking = (PkRanking)JSONObject.parseArray(str,PkRanking.class);
            rankingList.add(pkRanking);
        }
        sortList(rankingList);

        if (!rankingList.isEmpty() && rankingList.size() > 50){
            rankingList = rankingList.subList(0,50);
        }

        object.put("rankingList",rankingList);
        //mcoinUserPkInfoDao

        return object;
    }

    /**
     * 排序
     * @param rankingList
     */
    private void sortList(List<PkRanking> rankingList) {
        if (null == rankingList || rankingList.isEmpty()){
            return;
        }
        Collections.sort(rankingList, new Comparator<PkRanking>() {
            @Override
            public int compare(PkRanking o1, PkRanking o2) {
                Integer termCount1 = 0;
                Integer termCount2 = 0;
                if (o1 != null){
                    termCount1 = o1.getTermCount() == null ? 0 : o1.getTermCount();
                }
                if (o2 != null){
                    termCount2 = o2.getTermCount() == null ? 0 : o2.getTermCount();
                }

                return termCount2.compareTo(termCount1);
            }
        });
    }

    private void updateRankingUserData(int term,Long uid)throws WebServiceException {
        String scratchLockKey = MCOIN_PK_LOCK + "_" + term;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
        String userSql = "SELECT `uid` as uid,COUNT(carve_up_mcoin_num > 0 or NULL) as winCount, COUNT(`term`) as termCount, SUM(`carve_up_mcoin_num`) as carveUpMcoinNum, IF(COUNT(`term`) = 0, 0, ROUND((COUNT(carve_up_mcoin_num > 0 or NULL)/COUNT(`term`)) * 100,2)) as winRate, `create_time` as createTime FROM `mcoin_user_pk_info` WHERE term in (SELECT term FROM mcoin_pk_info WHERE pk_status = 2) AND `uid` = "+ uid;
        List<Map<String, Object>> userList = jdbcTemplate.queryForList(userSql);
        if (null != userList && !userList.isEmpty()){
            for(Map<String, Object> map:userList){
                redisManager.hset(RedisKey.mcoin_pk_history_gain + "_" + term, Long.toString(uid),null == map.get("carveUpMcoinNum") ? "0" : map.get("carveUpMcoinNum").toString());
                redisManager.hset(RedisKey.mcoin_pk_history_times + "_" + term, Long.toString(uid),null == map.get("termCount") ? "0" : map.get("termCount").toString());
                redisManager.hset(RedisKey.mcoin_pk_history_win_times + "_" + term, Long.toString(uid),null == map.get("winCount") ? "0" : map.get("winCount").toString());
                redisManager.hset(RedisKey.mcoin_pk_history_win_rate + "_" + term, Long.toString(uid),null == map.get("winRate") ? "0" : map.get("winRate").toString());
            }
        }
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(scratchLockKey, lockVal);
        }
    }

    private void updateRankingData(int term)throws WebServiceException {
        String scratchLockKey = MCOIN_PK_LOCK + "_" + term;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
        String dataSql = "SELECT info.uid as uid,u.nick as nick,u.avatar as avatar,COUNT(info.`term`) as termCount,IF(COUNT(info.`term`) = 0,0,ROUND((COUNT(info.carve_up_mcoin_num > 0 or NULL)/COUNT(info.`term`)) * 100,2)) as winRate FROM `mcoin_user_pk_info` info,users u WHERE u.uid = info.uid and term in (SELECT term FROM mcoin_pk_info WHERE pk_status = 2) GROUP BY info.uid ORDER BY termCount DESC";
        List<Map<String, Object>> rankingDatas = jdbcTemplate.queryForList(dataSql);
        if (null != rankingDatas && !rankingDatas.isEmpty()){
            for (Map<String, Object> map : rankingDatas) {
                JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(map));
                redisManager.zadd(RedisKey.mcoin_pk_history_ranking_list + "_" + term, System.currentTimeMillis() / 1000, itemJSONObj.toJSONString());
            }
        }
    } catch (Exception e) {
        throw e;
    } finally {
        redisManager.unlock(scratchLockKey, lockVal);
    }
    }

    private void updatePastPeriod(int term)throws WebServiceException {
        String scratchLockKey = MCOIN_PK_LOCK + "_" + term;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
        String dataSql = "SELECT term,title,CASE WHEN red_polls > blue_polls THEN red_answer WHEN red_polls < blue_polls THEN blue_answer WHEN red_polls = blue_polls THEN red_answer WHEN red_polls IS NULL OR blue_polls IS NULL THEN '' END 'answer' FROM mcoin_pk_info WHERE pk_status = 2 ORDER BY term desc limit 0,10";
        List<Map<String, Object>> periodDatas = jdbcTemplate.queryForList(dataSql);
        if (null != periodDatas && !periodDatas.isEmpty()){
            for (Map<String, Object> map : periodDatas) {
                Map<String, Object> resultMap = new LinkedHashMap<>();

                String numStr = numberToCH(null == map.get("term") ? 0 : Integer.parseInt(map.get("term").toString()));
                resultMap.put("termNum",null == map.get("term") ? 0 : Integer.parseInt(map.get("term").toString()));
                resultMap.put("term",numStr);
                resultMap.put("title",null == map.get("title") ? "" : map.get("title").toString());
                resultMap.put("answer",null == map.get("answer") ? "" : map.get("answer").toString());
                JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(resultMap));
                redisManager.zadd(RedisKey.mcoin_pk_history_period_list + "_" + term, System.currentTimeMillis() / 1000, itemJSONObj.toJSONString());
            }
        }
    } catch (Exception e) {
        throw e;
    } finally {
        redisManager.unlock(scratchLockKey, lockVal);
    }
    }

    /**
     * 删除上次排行榜数据
     * @param term
     * @param uid
     * @throws WebServiceException
     */
    private void deleteRedis(int term,Long uid) throws WebServiceException {
        String scratchLockKey = MCOIN_PK_LOCK + "_" + term;
        String lockVal = redisManager.lock(scratchLockKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            redisManager.hdel(RedisKey.mcoin_pk_history_gain + "_" + term, Long.toString(uid));
            redisManager.hdel(RedisKey.mcoin_pk_history_times + "_" + term, Long.toString(uid));
            redisManager.hdel(RedisKey.mcoin_pk_history_win_times + "_" + term, Long.toString(uid));
            redisManager.hdel(RedisKey.mcoin_pk_history_win_rate + "_" + term, Long.toString(uid));
            redisManager.del(RedisKey.mcoin_pk_history_ranking_list + "_" + term);
            redisManager.del(RedisKey.mcoin_pk_history_period_list + "_" + term);

        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(scratchLockKey, lockVal);
        }
    }

    /**
     * 开始新一期活动并群发小秘书
     * @param term
     */
    @Override
    public void pkBeginning(int term) {
        //查询新一期活动是否推送全服消息
        /*Byte pushMsgStatus = mcoinPkInfoDao.getPushMsgStatusByTerm(term);
        if (null == pushMsgStatus || pushMsgStatus.equals((byte)1)){
            logger.warn("已经推送全服PK活动第{}期开始消息",term);
            return;
        }*/
        //更新新一期活动状态为正在进行
        mcoinPkInfoDao.updateNextTermBeginning(term);
        //发送延迟消息
        eventSendDelayQueueMessage();
        //发全服小秘书通知用户活动开始
        NeteaseBatchMsgBO neteaseSendMsgBatchParam = new NeteaseBatchMsgBO();
        neteaseSendMsgBatchParam.setFromAccid(systemConf.getSecretaryUid());
        //
        StringBuilder sb = new StringBuilder("点点币PK第");
        sb.append(term);
        sb.append("期已开启了，快去点点币中心-点点币pk瓜分点点币啦");
        neteaseSendMsgBatchParam.setContent(sb.toString());
        msgPushManager.sendGroupMsg(neteaseSendMsgBatchParam);

    }

    @Override
    public JSONObject pastPeriod(Long uid) throws WebServiceException {
        JSONObject object = new JSONObject();
        //获取本期期号
        String termStr = redisManager.get(RedisKey.mcoin_pk_this_term + "");
        int term = termStr == null ? 0 : Integer.valueOf(termStr);
        Set<String> periodSet = redisManager.zrangeByScore(RedisKey.mcoin_pk_history_period_list + "_" + term, 0, System.currentTimeMillis() / 1000);
        if (null == periodSet || periodSet.isEmpty()) {
            updatePastPeriod(term);
        }

        List<Huigu> periodList = new ArrayList<>();
        for (String str : periodSet){
            Huigu pkRanking = (Huigu) JSON.parseObject(str, Huigu.class);
            //PkRanking pkRanking = (PkRanking)JSONObject.parseArray(str,PkRanking.class);
            periodList.add(pkRanking);
        }

        sortPeriodList(periodList);
        object.put("periodList",periodList);

        return object;
    }

    /**
     * 发送延迟消息
     */
    @Override
    public void eventSendDelayQueueMessage() {
        //根据活动状态获取到状态正在进行的活动信息
        McoinPkInfoDTO mcoinPkInfoDTO = mcoinPkInfoDao.getForStatusNormal((byte)1);
        if(mcoinPkInfoDTO != null) {
            //获取本期的期号
            int item = mcoinPkInfoDTO.getTerm();
            //获取本期的开奖时间
            Date lotteryTime = mcoinPkInfoDTO.getLotteryTime();
            //获取本次到开奖时间的毫秒差
            Long endTime = lotteryTime.getTime() - System.currentTimeMillis();

            // 获取redis用于执行开奖的标记，该redis只=1
            long tag = redisManager.hincrBy(RedisKey.mcoin_pk_lottery_tag + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm() + "", 1L);
            if (tag == 1) {
                redisManager.hincrBy(RedisKey.mcoin_pk_lottery_tag + "_" + mcoinPkInfoDTO.getTerm(), mcoinPkInfoDTO.getTerm() + "", 1L);
                //设置redis的过期时间
                redisManager.expire(RedisKey.mcoin_pk_lottery_tag + "_" + mcoinPkInfoDTO.getTerm(), (int) (endTime / 3600000) + 1, TimeUnit.HOURS);
                //进行MQ延时处理开奖
                JSONObject object = new JSONObject();
                object.put("item", item);
                object.put("endTime", endTime);
                if (endTime > 0) {
                    activeMqManager.sendDelayQueueMessage(MqDestinationKey.MCOIN_PK_QUEUE, object.toJSONString(), endTime);
                }
            }
        }

    }


    /**
     * 排序
     * @param periodList
     */
    private void sortPeriodList(List<Huigu> periodList) {

        if (null == periodList || periodList.isEmpty()){
            return;
        }

        Collections.sort(periodList, new Comparator<Huigu>() {
            @Override
            public int compare(Huigu o1, Huigu o2) {
                Integer termNum1 = 0;
                Integer termNum2 = 0;
                if (o1 != null) {
                    termNum1 = o1.getTermNum() == null ? 0 : o1.getTermNum();
                }
                if (o2 != null){
                    termNum2 = o2.getTermNum() == null ? 0 : o2.getTermNum();
                }
                return termNum2.compareTo(termNum1);
            }
        });
    }

    private String formatNum(int num){
        String temp = "***";
        if (num >= 0){
            if (num < 10){
                temp = temp +"0"+ num;
            }else if (num >= 10 ){
                temp = temp + (num/10%10) + (num%10);
            }
        }else{
            temp = "***00";
        }
        return temp;
    }

    //将阿拉伯数字转化为中文数字
    public static String numberToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1")) {
                if (intInput > 100){
                    sd += "一十";
                }else{
                    sd += "十";
                }
            } else {
                sd += (GetCH(intInput / 10) + "十");
            }
            sd += numberToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2) {
                sd += "零";
            }
            sd += numberToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3) {
                sd += "零";
            }
            sd += numberToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4) {
                sd += "零";
            }
            sd += numberToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    public static void main(String[] args) {
        Integer aa = 11;
        String _number = numberToCH(aa);
        System.out.println(_number);
    }
}

@Getter
@Setter
class PkRanking{

    private Long uid;
    private String nick;
    private String avatar;
    private Integer termCount;
    private Double winRate;
}

@Getter
@Setter
class Huigu{
    private Integer termNum;
    private String term;
    private String title;
    private String answer;
}

