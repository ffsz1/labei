package com.erban.main.service;

import com.ctc.wstx.util.StringUtil;
import com.erban.main.model.AuctionCur;
import com.erban.main.util.StringUtils;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuguofu on 2017/7/10.
 */
@Service
public class AppStoreService {
    @Autowired
    private JedisService jedisService;

    public BusiResult<Boolean> checkVersion(String version) {
        Boolean result=false;
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        String auditingVersion=jedisService.read(RedisKey.auditing_iosversion.getKey());
        if(version.equals(auditingVersion)){
            result=true;
        }
        busiResult.setData(result);
        return busiResult;
    }

    public boolean checkVersionBy(String version){
        boolean result=false;
        if(StringUtils.isEmpty(version)){
            return result;
        }
        String auditingVersion=jedisService.read(RedisKey.auditing_iosversion.getKey());
        if(version.equals(auditingVersion)){
            result=true;
        }
        return result;
    }

    public BusiResult<Boolean> checkIsNeedForceUpdate(String version) {
        Boolean result=false;
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        String forceupdateVersion=jedisService.read(RedisKey.forceupdate_iosversion.getKey());
        if(StringUtils.isBlank(forceupdateVersion)){//数据为空，没有强制更新版本，不需要强制更新，直接返回false
            result= false;
        }else{
            if(version.equals(forceupdateVersion)){//有需要强制更新版本，当前版本与强制版本一致，不需要更新
                result=false;
            }else{//有需要强制更新版本，当前版本与强制版本不一直，需要强制更新，返回false
                result=true;
            }
        }
        busiResult.setData(result);
        return busiResult;
    }



}
