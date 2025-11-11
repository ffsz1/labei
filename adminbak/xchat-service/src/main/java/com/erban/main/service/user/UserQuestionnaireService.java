package com.erban.main.service.user;

import com.erban.main.model.BillRecord;
import com.erban.main.model.UserQuestionnaireRecord;
import com.erban.main.mybatismapper.UserQuestionnaireRecordMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.util.StringUtils;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserQuestionnaireService extends BaseService{
    @Autowired
    private UserQuestionnaireRecordMapper userQuestionnaireRecordMapper;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;

    private int getOne(Long uid, Integer type) {
        Integer num = jdbcTemplate.queryForObject("select COUNT(1) from user_questionnaire_record where uid = ? and type = ?", Integer.class, uid, type);
        if(num==0){
            return 0;
        }else{
            String draw = jedisService.hget(RedisKey.questionnaire_draw.getKey(), uid.toString()+type.toString());
            if(StringUtils.isBlank(draw)){
                return 1;
            }
            return 2;
        }
    }

    public BusiResult get(Long uid) {
        return new BusiResult(BusiStatus.SUCCESS, getOne(uid, 1));
    }

    public BusiResult save(Long uid, String list) {
        Map<String, String> map = new HashMap<>();
        map = gson.fromJson(list, map.getClass());
        if(map==null||map.keySet().size()!=20){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        if(getOne(uid, 1)!=0){
            return new BusiResult(BusiStatus.NOAUTHORITY, "您已经填写过这份问卷");
        }
        UserQuestionnaireRecord userQuestionnaireRecord;
        Date date = new Date();
        for(String key:map.keySet()){
            userQuestionnaireRecord=new UserQuestionnaireRecord();
            userQuestionnaireRecord.setUid(uid);
            userQuestionnaireRecord.setType(1);
            userQuestionnaireRecord.setQuestion(Integer.valueOf(key));
            userQuestionnaireRecord.setAnswer(map.get(key));
            userQuestionnaireRecord.setCreateTime(date);
            userQuestionnaireRecordMapper.insert(userQuestionnaireRecord);
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    private static double genDoubleNumber() {
        Random ra = new Random();
        double dd = ra.nextDouble();
        DecimalFormat doubleFormat = new DecimalFormat("0.0000");
        double number = new Double(doubleFormat.format(dd));
        return number;
    }

    public BusiResult draw(Long uid) {
        if(getOne(uid, 1)!=1){
            return new BusiResult(BusiStatus.NOAUTHORITY, "您已经获取过神秘礼物");
        }
        double randomNumber = genDoubleNumber();
        double[] drawPrize = Constant.QuestionnaireRange.questionnaire;//中奖机会：10/20/50金币
        Integer result = 0;
        if(randomNumber>=drawPrize[0]&&randomNumber<=drawPrize[1]){
            result = Constant.QuestionnaireProd.gold10;
        }else if (randomNumber > drawPrize[1] && randomNumber <= drawPrize[2]) {
            result = Constant.QuestionnaireProd.gold20;
        } else if (randomNumber > drawPrize[2] && randomNumber <= drawPrize[3]) {
            result = Constant.QuestionnaireProd.gold50;
        }
        userPurseUpdateService.addGoldDbAndCache(uid, result.longValue());
        jedisService.hset(RedisKey.questionnaire_draw.getKey(), uid.toString()+"1", "1");
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId("1");
        billRecord.setObjType(Constant.BillType.questionnaireDraw);
        billRecord.setGoldNum(result.longValue());// 记录每次抽奖的金额
        billRecord.setCreateTime(new Date());
        billRecordService.insertBillRecord(billRecord);
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

}
