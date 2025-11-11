package com.juxiao.xchat.manager.common.sysconf.impl;

import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.common.sysconf.GeneralManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chris
 * @Title:
 * @date 2019-05-30
 * @time 16:46
 */

@Service
public class GeneralManagerImpl implements GeneralManager {

    @Autowired
    private SysConfManager sysConfManager;



    @Override
    public boolean checkProhibitModification() {
        SysConfDTO conf = sysConfManager.getSysConf(SysConfigId.prohibit_modification);
        if(conf == null){
            return false;
        }
        if("1".equals(conf.getConfigValue())){
            return true;
        }
        return false;
    }
}
