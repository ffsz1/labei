package com.erban.main.service;

import com.erban.main.model.Deposit;
import com.erban.main.model.DepositExample;
import com.erban.main.model.UserPurse;
import com.erban.main.mybatismapper.DepositMapper;
import com.erban.main.service.user.UserPurseService;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/7/3.
 */
@Service
public class DepositService {
    private static final Logger logger = LoggerFactory.getLogger(DepositService.class);

    @Autowired
    private DepositMapper depositMapper;

    @Autowired
    private UserPurseService userPurseService;


    @Transactional(rollbackFor=Exception.class)
    public boolean updateOrInsertDeposit(Long uid,String objId,Byte useType,Long depositGoldMoneyNum) throws Exception{
        if(depositGoldMoneyNum<1){
            return false;
        }

        UserPurse userPurse=userPurseService.getPurseByUid(uid);
        Long currentRealPurseGold=userPurse.getGoldNum();
        boolean insertDepositFlag=false;
        int plusOrReplace=0;
        Date date=new Date();
        Deposit currentDeposit=null;
        List<Deposit> depositList=getDepositByUid(uid);
        if(CollectionUtils.isEmpty(depositList)){//当前没有任何预扣款记录
            insertDepositFlag=true;
            plusOrReplace=Constant.DepositUpdateType.plus;
        }else{//存在记录

            if(depositList.size()==1){//只有一条预扣款记录
                currentDeposit=getCurrentDepoist(depositList,objId);
                if(currentDeposit==null){
                    insertDepositFlag=true;
                    plusOrReplace=Constant.DepositUpdateType.plus;
                }else{//更新预扣款记录
                    currentRealPurseGold=currentDeposit.getGoldNum()+currentRealPurseGold;
                    insertDepositFlag=false;
                    plusOrReplace=Constant.DepositUpdateType.replace;
                }
            }else{//当前有多条预扣款记录
                currentDeposit=getCurrentDepoist(depositList,objId);
                if(currentDeposit==null){
                    insertDepositFlag=true;
                    plusOrReplace=Constant.DepositUpdateType.plus;
                }else{//更新预扣款记录
                    currentRealPurseGold=currentDeposit.getGoldNum()+currentRealPurseGold;
                    insertDepositFlag=false;
                    plusOrReplace=Constant.DepositUpdateType.plus;
                }
            }

        }
        if(currentRealPurseGold<depositGoldMoneyNum){
            return false;
        }
        if(insertDepositFlag){
            currentDeposit=new Deposit();
            currentDeposit.setDid(UUIDUitl.get());
            currentDeposit.setGoldNum(depositGoldMoneyNum);//支付需要交的预扣款
            currentDeposit.setObjId(objId);
            currentDeposit.setUseType(useType);
            currentDeposit.setCurStatus(Constant.DepositStatus.create);
            currentDeposit.setUid(uid);
            currentDeposit.setCreateTime(date);
            depositMapper.insert(currentDeposit);
            userPurseService.updateGoldByDeposite(uid,depositGoldMoneyNum,plusOrReplace);
        }else{
            currentDeposit.setGoldNum(depositGoldMoneyNum);
            depositMapper.updateByPrimaryKeySelective(currentDeposit);
            userPurseService.updateGoldByDeposite(uid,depositGoldMoneyNum,plusOrReplace);
        }

        return true;
    }
    private Deposit getCurrentDepoist(List<Deposit> depositList,String objId){
        for(Deposit deposit:depositList){
            if(deposit.getObjId().equals(objId)){
                return deposit;
            }
        }
        return null;

    }
    @Transactional(rollbackFor=Exception.class)
    public boolean deleteDepositByObjId(String objId,Long uid){
        DepositExample depositExample=new DepositExample();
        DepositExample.Criteria criteria=depositExample.createCriteria();
        criteria.andUidEqualTo(uid).andObjIdEqualTo(objId);
        depositMapper.deleteByExample(depositExample);
        return true;
    }

    private Deposit getDepositBy(Long uid,String objId,Byte useType){
        DepositExample depositExample=new DepositExample();
        DepositExample.Criteria criteria=depositExample.createCriteria();
        criteria.andUidEqualTo(uid).andObjIdEqualTo(objId).andUseTypeEqualTo(useType);
        List<Deposit> depositList=depositMapper.selectByExample(depositExample);
        if(CollectionUtils.isEmpty(depositList)){
            return null;
        }else{
            return depositList.get(0);
        }

    }
    private List<Deposit> getDepositByUid(Long uid){
        DepositExample depositExample=new DepositExample();
        DepositExample.Criteria criteria=depositExample.createCriteria();
        criteria.andUidEqualTo(uid);
        List<Deposit> depositList=depositMapper.selectByExample(depositExample);
        return depositList;
    }



}
