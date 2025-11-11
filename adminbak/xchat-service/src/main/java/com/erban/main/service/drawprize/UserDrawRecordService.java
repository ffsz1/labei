package com.erban.main.service.drawprize;

import com.erban.main.model.UserDrawRecord;
import com.erban.main.model.UserDrawRecordExample;
import com.erban.main.mybatismapper.UserDrawRecordMapper;
import com.xchat.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserDrawRecordService {
    @Autowired
    private UserDrawRecordMapper userDrawRecordMapper;

    /**
     * 生成抽奖机会，分享，只能有一次抽奖；每次充值，都生成一次抽奖机会
     * @param uid
     * @param type
     * @param srcObjAmount
     * @param srcObjId
     * @param srcObjName
     */
    public UserDrawRecord genUserDrawRecord(Long uid,Byte type,Long srcObjAmount ,String srcObjId,String srcObjName){
        UserDrawRecord userDrawRecord=new UserDrawRecord();
        userDrawRecord.setUid(uid);
        userDrawRecord.setType(type);
        userDrawRecord.setSrcObjAmount(srcObjAmount);
        userDrawRecord.setDrawStatus(Constant.DrawStatus.create);
        userDrawRecord.setSrcObjId(srcObjId);
        userDrawRecord.setSrcObjName(srcObjName);
        Date date=new Date();
        userDrawRecord.setCreateTime(date);
        userDrawRecordMapper.insertSelective(userDrawRecord);
        return userDrawRecord;
    }

    /**
     * 获取用户未使用的抽奖机会，用于抽奖
     * @param uid
     * @return
     */
    public UserDrawRecord getUserDrawRecordByUidOfCreate(Long uid){
        UserDrawRecordExample userDrawRecordExample=new UserDrawRecordExample();
        userDrawRecordExample.createCriteria().andUidEqualTo(uid).andDrawStatusEqualTo(Constant.DrawStatus.create);
        List<UserDrawRecord> userDrawRecordList=userDrawRecordMapper.selectByExample(userDrawRecordExample);
        if(CollectionUtils.isEmpty(userDrawRecordList)){
            return null;
        }else{
            return userDrawRecordList.get(0);
        }

    }

    public void updateUserDrawRrecord(UserDrawRecord userDrawRecord){
        userDrawRecordMapper.updateByPrimaryKeySelective(userDrawRecord);

    }


}
