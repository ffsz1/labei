package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.AppVersionMapper;
import com.erban.main.mybatismapper.AppVersionUpdateConfMapper;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.AppVersionVo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/7/10.
 */
@Service
public class AppVersionUpdateConfService {
    @Autowired
    private JedisService jedisService;
    @Autowired
    private AppVersionUpdateConfMapper appVersionUpdateConfMapper;



    private Gson gson=new Gson();

    public List<AppVersionUpdateConf> getAppUpdateVersionConfList(){
        List<AppVersionUpdateConf> appVersionUpdateList= getAllAppVersionUpdateConfList();
        return appVersionUpdateList;
    }

    private List<AppVersionUpdateConf> queryAllAppVersionUpdateConfList(){
        AppVersionUpdateConfExample appVersionUpdateConfExample=new AppVersionUpdateConfExample();
        List<AppVersionUpdateConf> appVersionUpdateConfList=appVersionUpdateConfMapper.selectByExample(appVersionUpdateConfExample);
        return appVersionUpdateConfList;
    }
    private List<AppVersionUpdateConf> getAllAppVersionUpdateConfList() {
        List<AppVersionUpdateConf> appVersionUpdateConfList =Lists.newArrayList();
        Map<String, String> appVersionUpdateConfMap = jedisService.hgetAllBykey(RedisKey.app_version_update.getKey());
        if (appVersionUpdateConfMap == null || appVersionUpdateConfMap.size() == 0) {
            return appVersionUpdateConfList;
        }
        Iterator<Map.Entry<String, String>> it = appVersionUpdateConfMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                AppVersionUpdateConf appVersionUpdateConf = gson.fromJson(value, AppVersionUpdateConf.class);
                appVersionUpdateConfList.add(appVersionUpdateConf);
            }
        }

        return appVersionUpdateConfList;
    }

    public void refreshAppVersionUpdateConf(){
        List<AppVersionUpdateConf> appVersionUpdateConfList =queryAllAppVersionUpdateConfList();
        if(CollectionUtils.isEmpty(appVersionUpdateConfList)){
            return;
        }
        batchSaveAppVersionUpdateConfCache(appVersionUpdateConfList);
    }

    private void batchSaveAppVersionUpdateConfCache(List<AppVersionUpdateConf> appVersionUpdateConfList) {
        if (CollectionUtils.isEmpty(appVersionUpdateConfList)) {
            return;
        }
        for (AppVersionUpdateConf appVersionUpdateConf : appVersionUpdateConfList) {
            saveAppVersionUpdateCache(appVersionUpdateConf);
        }
    }
    private void saveAppVersionUpdateCache(AppVersionUpdateConf appVersionUpdateConf) {
        if(appVersionUpdateConf==null){
            return;
        }
        String version = appVersionUpdateConf.getVersion();
        String replace = version.replace(".", "");
        int versionInt = Integer.parseInt(replace);
        if(versionInt<230){
           return;
        }
        String key=appVersionUpdateConf.getVersion()+appVersionUpdateConf.getOs();
        jedisService.hwrite(RedisKey.app_version_update.getKey(), key, gson.toJson(appVersionUpdateConf));
    }


}
