package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.DrawProd;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.spring.SpringAppContext;
import com.juxiao.xchat.base.utils.DataUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGoldDrawDao;
import com.juxiao.xchat.dao.bill.domain.BillGoldDrawDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.user.UserDrawPrettyErbanNoDao;
import com.juxiao.xchat.dao.user.UserDrawRecordDao;
import com.juxiao.xchat.dao.user.UserWordGetRecordDao;
import com.juxiao.xchat.dao.user.domain.UserDrawRecordDO;
import com.juxiao.xchat.dao.user.domain.UserWordDrawOverviewDO;
import com.juxiao.xchat.dao.user.domain.UserWordGetRecordDO;
import com.juxiao.xchat.dao.user.dto.*;
import com.juxiao.xchat.dao.user.enumeration.*;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UserWordDrawManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.UserDrawService;
import com.juxiao.xchat.service.api.user.UserWordDrawService;
import com.juxiao.xchat.service.api.user.draw.WordDrawService;
import com.juxiao.xchat.service.api.user.vo.UserDrawResultVO;
import com.juxiao.xchat.service.api.user.vo.UserWordDrawResultVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class UserWordDrawServiceImpl implements UserWordDrawService {

    private static final Logger logger = LoggerFactory.getLogger(UserWordDrawServiceImpl.class);

    @Autowired
    private UserDrawService userDrawService;
    @Autowired
    private UserWordDrawManager userWordDrawManager;
    @Autowired
    private UserDrawManager userDrawManager;
    @Autowired
    private UserWordGetRecordDao userWordGetRecordDao;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private UserDrawRecordDao drawRecordDao;
    @Autowired
    private BillGoldDrawDao goldDrawDao;
    @Autowired
    private UserDrawPrettyErbanNoDao prettyErbanNoDao;
    @Autowired
    private GiftCarManager giftCarManager;


    private Map<Integer, String> activityWordDrawMap = new HashMap<>();

    @PostConstruct
    public void init() {
        this.activityWordDrawMap.put(UserWordDrawActivityType.NIU_DAN.getType(), "niuDanWordDraw");
    }

    @Override
    public UserWordDrawResultVO getUserWordDraw(Long uid, Integer activityType) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UserWordDrawOverviewDTO overviewDTO = userWordDrawManager.getUserWordDrawOverview(uid, activityType);


        List<UserWordDrawDTO> list = userWordDrawManager.getUnUsedWordList(uid, activityType);
        List<String> words = new ArrayList<>();
        list.forEach(node -> words.add(node.getWord()));
        List<String> allWords = UserWordDrawWordType.getActivityAllCode(activityType);
        UserWordDrawResultVO resultVO = new UserWordDrawResultVO();
        resultVO.setCollectedWords(words);
        resultVO.setAllWords(allWords);
//        resultVO.setLeftDrawNum(overviewDTO.getLeftDrawNum());
//        resultVO.setTotalDrawNum(overviewDTO.getTotalDrawNum());

        byte drawStatus = UserDrawRecordStatus.HAS_PRIZE.getValue();
        for (String w : allWords) {
            if (!words.contains(w)) {
                drawStatus = UserDrawRecordStatus.NONE_PRIZE.getValue();
                break;
            }
        }
        resultVO.setDrawStatus(drawStatus);
        return resultVO;
    }


    @Override
    public UserWordDrawResultVO drawWord(Long uid, Integer activityType) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UserWordDrawOverviewDTO overviewDTO = userWordDrawManager.getUserWordDrawOverview(uid, activityType);
        if (overviewDTO.getLeftDrawNum() <= 0) {
            throw new WebServiceException(WebServiceCode.NO_MORE_CHANCE);
        }

        WordDrawService wordDrawService = SpringAppContext.getBean(activityWordDrawMap.get(activityType), WordDrawService.class);
        if (wordDrawService == null) {
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }
        // 给抽奖用户添加一个锁
        String lockValue = redisManager.lock(RedisKey.user_word_draw_lock.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lockValue)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            UserWordDrawWordType wordType = wordDrawService.doDraw(uid);
            if (wordType == null) {
                return null;
            } else {
                userWordDrawManager.saveDrawWord(uid, wordType);

                overviewDTO.setLeftDrawNum(overviewDTO.getLeftDrawNum() - 1);
                overviewDTO.setTotalDrawNum(overviewDTO.getTotalDrawNum());

                UserWordDrawOverviewDO userWordDrawOverviewDO = new UserWordDrawOverviewDO();
                BeanUtils.copyProperties(overviewDTO, userWordDrawOverviewDO);
                userWordDrawManager.saveUserWordDrawOverview(userWordDrawOverviewDO);

                //记录纪录
                UserWordGetRecordDO recordDO = new UserWordGetRecordDO();
                recordDO.setUid(uid);
                recordDO.setWord(wordType.getWord());
                recordDO.setActivityType(wordType.getActivityType().getType());
                recordDO.setCreateTime(new Date());
                userWordGetRecordDao.insert(recordDO);

                boolean isCollectAll = checkIsCollectAllWord(uid, activityType);
                //判断是否已集合所有的字体
                if (isCollectAll
                        //同一活动，一个用户最多只能生成一个有效的抽奖单
                        && drawRecordDao.getUserCreateDrawRecord3(uid, UserDrawRecordType.WORD_DRAW.getValue(), wordType.getActivityType().getType() + "") == null
                ) {
                    userDrawManager.saveUserDrawRecord(uid, UserDrawRecordType.WORD_DRAW, null, wordType.getActivityType().getType() + "", "扭蛋机抽奖");
                }
                UserWordDrawResultVO resultVO = new UserWordDrawResultVO();
                resultVO.setUid(uid);
                resultVO.setLeftDrawNum(overviewDTO.getLeftDrawNum());
                resultVO.setTotalDrawNum(overviewDTO.getTotalDrawNum());
                resultVO.setWord(wordType.getWord());

                List<UserWordDrawDTO> list = userWordDrawManager.getUnUsedWordList(uid, activityType);
                List<String> words = new ArrayList<>();
                list.forEach(node -> words.add(node.getWord()));

                List<String> allWords = UserWordDrawWordType.getActivityAllCode(activityType);
                resultVO.setCollectedWords(words);
                resultVO.setAllWords(allWords);
                if (isCollectAll) {
                    resultVO.setDrawStatus(UserDrawRecordStatus.HAS_PRIZE.getValue());
                } else {
                    resultVO.setDrawStatus(UserDrawRecordStatus.NONE_PRIZE.getValue());
                }
                return resultVO;
            }
        } catch (Exception e) {
            logger.error("文字抽奖失败,uid:{}, activityType:{}, msg:{}", uid, activityType, e.getMessage(), e);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        } finally {
            redisManager.unlock(RedisKey.user_word_draw_lock.getKey(uid.toString()), lockValue);
        }
    }

    @Override
    public UserDrawRecordDO createDrawPrize(UserDrawRecordDTO recordDto) throws WebServiceException {

        List<UserWordDrawPrizeConfDTO> confList = userWordDrawManager.getPrizeConfList(Integer.parseInt(recordDto.getSrcObjId()));
        if (CollectionUtils.isEmpty(confList)) {
            logger.error("文字抽奖没有奖品");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }
        //只选择第一个有效的奖品
        int type = confList.get(0).getPrizeType().intValue();
        UserWordDrawPrizeConfDTO prizeConf = confList.get(0);
        UserDrawRecordDO recordDo = initRecordDo(recordDto.getRecordId());
        if (type == UserWordDrawPrizeType.GOLD.getType()) {

            recordDo.setDrawPrizeId(prizeConf.getPrizeObjId());
            recordDo.setDrawPrizeName(prizeConf.getPrizeGoldAmount() + "金币");

        } else if (type == UserWordDrawPrizeType.ERBAN_NO.getType()) {
            UserDrawPrettyErbanNoDao prettyErbanNoDao = SpringAppContext.getBean(UserDrawPrettyErbanNoDao.class);
            UserDrawPrettyErbanNoDTO prettyErbanNo = prettyErbanNoDao.getNotUsePrettyErbanNo(Byte.parseByte("1"));
            if (prettyErbanNo != null) {
                recordDo.setDrawPrizeName("七位靓号" + prettyErbanNo.getPrettyErbanNo());
                recordDo.setDrawPrizeId(DrawProd.prettySeven);
            } else {
                logger.warn("文字抽奖 没有七位靓号， 采用默认金币");
                recordDo.setDrawPrizeId(DrawProd.gold1000);
                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
            }

        } else if (type == UserWordDrawPrizeType.CAR.getType()) {

            logger.error("文字抽奖 奖品类型不支持");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);

//            GiftCarDTO giftCarDTO = null;
//            if(prizeConf.getPrizeObjId() != null){
//                giftCarDTO = giftCarManager.getGiftCar(confList.get(0).getPrizeObjId());
//            }
//            if(giftCarDTO != null){
//                // 保存用户座驾信息
//                giftCarManager.saveUserCar(recordDto.getUid(), giftCarDTO.getCarId(), giftCarDTO.getEffectiveTime().intValue(),
//                        CarGetType.user_draw.getValue(), null);
//            }else{
//                logger.warn("文字抽奖 没有找到座驾信息， 采用默认金币");
//                recordDo.setDrawPrizeId(DrawProd.gold1000);
//                recordDo.setDrawPrizeName(DrawProd.gold1000 + "金币");
//            }

        } else {
            logger.error("文字抽奖 奖品类型不支持");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }
        return recordDo;
    }


    @Override
    public UserDrawResultVO draw(Long uid, Integer activity) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        //判断是否已集合所有的字体
        if (!checkIsCollectAllWord(uid, activity)) {
            throw new WebServiceException(WebServiceCode.NO_COLLECT_WORD_FINISH);
        }

        // 给抽奖用户添加一个锁
        String lockValue = redisManager.lock(RedisKey.user_word_draw_lock.getKey(uid.toString()), 5 * 1000);
        if (StringUtils.isBlank(lockValue)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            UserDrawRecordDTO drawRecordDto = drawRecordDao.getUserCreateDrawRecord3(uid, UserDrawRecordType.WORD_DRAW.getValue(), activity + "");
            if (drawRecordDto == null) {
                throw new WebServiceException(WebServiceCode.NO_COLLECT_WORD_FINISH);
            }
            UserDrawRecordDO drawRecordDo = createDrawPrize(drawRecordDto);

            UserWordDrawOverviewDTO drawOverviewDTO = userWordDrawManager.getUserWordDrawOverview(uid, activity);

            drawOverviewDTO.setWinDrawNum(drawOverviewDTO.getWinDrawNum() + 1);


            if (drawRecordDo.getDrawPrizeId() <= DrawProd.gold8888) {// 金币充值，增加金币账单

                userPurseManager.updateAddGold(uid, drawRecordDo.getDrawPrizeId().longValue(), false, true, "兑换文字抽奖奖品", null, null);

                BillGoldDrawDO goldDrawDo = new BillGoldDrawDO();
                goldDrawDo.setRecordId(drawRecordDo.getRecordId().longValue());
                goldDrawDo.setUid(drawRecordDto.getUid());
                goldDrawDo.setGoldAmount(drawRecordDo.getDrawPrizeId());
                goldDrawDo.setCreateTime(new Date());
                goldDrawDao.save(goldDrawDo);

                // FIXME: 兼容管理后台
                userDrawService.insertBillRecord(uid, String.valueOf(drawRecordDo.getRecordId()), BillRecordType.draw, drawRecordDo.getDrawPrizeId());
            } else if (drawRecordDo.getDrawPrizeId() <= DrawProd.prettySeven) {// 修改抽奖靓号的状态
                prettyErbanNoDao.updateUsedPrettyErbanNo(DataUtils.getNumer(drawRecordDo.getDrawPrizeName()));
                drawRecordDo.setDrawPrizeName("七位靓号" + drawRecordDo.getDrawPrizeName());
            }
            //更改中奖字体的状态
            userWordDrawManager.updateUserWordStatus(uid, activity, true);

            UserWordDrawOverviewDO wordDrawOverviewDO = new UserWordDrawOverviewDO();
            BeanUtils.copyProperties(drawOverviewDTO, wordDrawOverviewDO);
            userWordDrawManager.saveUserWordDrawOverview(wordDrawOverviewDO);
            drawRecordDao.update(drawRecordDo);

            UserDrawResultVO resultVo = new UserDrawResultVO();
            resultVo.setTotalDrawNum(drawOverviewDTO.getTotalDrawNum());
            resultVo.setLeftDrawNum(drawOverviewDTO.getLeftDrawNum());

            resultVo.setDrawStatus(drawRecordDo.getDrawStatus());
            resultVo.setDrawPrizeName(drawRecordDo.getDrawPrizeName());
            resultVo.setSrcObjName(drawRecordDo.getSrcObjName());
            resultVo.setDrawPrizeId(drawRecordDo.getDrawPrizeId());

            return resultVo;
        } finally {
            redisManager.unlock(RedisKey.user_word_draw_lock.getKey(uid.toString()), lockValue);
        }
    }

    private UserDrawRecordDO initRecordDo(Integer recordId) {
        UserDrawRecordDO recordDo = new UserDrawRecordDO();
        recordDo.setRecordId(recordId);
        recordDo.setDrawStatus(UserDrawRecordStatus.HAS_PRIZE.getValue());
        recordDo.setType(UserDrawRecordType.WORD_DRAW.getValue());
        recordDo.setUpdateTime(new Date());
        return recordDo;
    }


    /**
     * 判断是否已收集全部的字体
     *
     * @param uid
     * @param activityType
     * @return
     */
    private boolean checkIsCollectAllWord(Long uid, Integer activityType) {
        List<UserWordDrawDTO> list = userWordDrawManager.getUnUsedWordList(uid, activityType);
        List<String> allWords = UserWordDrawWordType.getActivityAllCode(activityType);
        List<String> exists = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else {
            list.forEach(node -> exists.add(node.getWord()));
            for (String t : allWords) {
                if (!exists.contains(t)) {
                    return false;
                }
            }
            return true;
        }
    }


}
