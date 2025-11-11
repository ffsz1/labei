package com.juxiao.xchat.service.task.mora.impl;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mora.MoraRecordDAO;
import com.juxiao.xchat.dao.mora.domain.MoraRecordDO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.task.mora.MoraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-04
 * @time 17:51
 */
@Slf4j
@Service
public class TaskMoraServiceImpl implements MoraService {

    @Autowired
    private MoraRecordDAO moraRecordDAO;

    @Autowired
    private SysConfManager sysConfManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;


    /**
     * 检测过期
     */
    @Override
    public void checkExpired() {
        List<MoraRecordDO> moraRecordDOS =  moraRecordDAO.selectByValidMoraRecord();
        if(moraRecordDOS.size() > 0){
            moraRecordDOS.forEach(item ->{
                if(checkTime(item.getCreateTime())){
                    item.setIsValid(2);
                    item.setIsFinish(1);
                    item.setIsReturnGold(1);
                    moraRecordDAO.updateById(item);
                    try {
                        userPurseManager.updateAddGold(item.getUid(), item.getTotal(), false,false,"无人参与猜拳金币返回", null, null);
                        asyncNetEaseTrigger.sendMsg(item.getUid().toString(),"您太厉害了,无人敢与你猜拳,金币已退回!");
                    } catch (WebServiceException e) {
                        log.error("猜拳金币退回出现异常,异常信息:{}",e);
                    }
                }
            });
        }
    }


    private boolean checkTime(Date time){
        SysConfDTO sysConfDTO = sysConfManager.getSysConf(SysConfigId.mora_timeout);
        int timeOut;
        if(sysConfDTO == null){
            timeOut = 5;
        }else{
            timeOut = Integer.valueOf(sysConfDTO.getConfigValue());
        }
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        Calendar c3=Calendar.getInstance();
        //要判断的日期
        c1.setTime(time);
        //初始日期
        c2.setTime(new Date());
        //也给初始日期 把分钟加五
        c3.setTime(new Date());
        c3.add(Calendar.MINUTE, timeOut);
        //减去五分钟
        c2.add(Calendar.MINUTE,-timeOut);
        System.out.println("c1"+c1.getTime());
        System.out.println("c2"+c2.getTime());
        System.out.println("c3"+c3.getTime());
        return !c1.after(c2) || !c1.before(c3);
    }
}
