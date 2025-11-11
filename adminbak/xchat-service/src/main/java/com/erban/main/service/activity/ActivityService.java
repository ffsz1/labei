package com.erban.main.service.activity;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.AppActivity;
import com.erban.main.model.AppActivityExample;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.AppActivityMapper;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.ActVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private JedisService jedisService;
    @Autowired
    private AppActivityMapper appActivityMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UsersService usersService;
    private Gson gson=new Gson();

    /**
     * 获取拉贝官方活动
     * @param alertWinLoc 弹窗位置：1.首页，2.直播间
     * @return
     */
    public BusiResult<List<ActVo>> queryActivityList(Integer alertWinLoc, Long uid) {
        List<ActVo> actVos= Lists.newArrayList();
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        if(uid!=null){
            UserPurse uuserPurseBefore = userPurseService.getPurseByUid(uid);
            boolean isFirstCharge = uuserPurseBefore.getIsFirstCharge();
            if (isFirstCharge) {// 第一次充值的手机显示首冲活动
                Users users = usersService.getUsersByUid(uid);
                if(users!=null) {
                    String str = jedisService.hget(RedisKey.first_charge.getKey(), uid.toString());
                    if (StringUtils.isBlank(str)) {
                        ActVo actVo=new ActVo();
                        actVo.setActId(0);
                        actVo.setActName("新手礼包");
                        actVo.setAlertWin(true);
                        actVo.setAlertWinLoc(new Byte("2"));
                        actVo.setAlertWinPic("http://res.91fb.com/icon-new.png");
                        actVo.setSkipType(new Byte("1"));
                        actVo.setActAlertVersion("1.0.0");
                        actVo.setSkipUrl("https://www.47huyu.cn/mm/newgift/index.html");
                        actVos.add(actVo);
                        busiResult.setData(actVos);
                        return busiResult;
                    }
                }
            }
        }
        String actListStr=jedisService.hget(RedisKey.act.getKey(), alertWinLoc.toString());
        if(StringUtils.isEmpty(actListStr)){
            AppActivityExample appActivityExample = new AppActivityExample();
            appActivityExample.createCriteria().andActStatusEqualTo(Constant.ActStatus.using).andAlertWinLocEqualTo(alertWinLoc.byteValue());
            List<AppActivity> activities=appActivityMapper.selectByExample(appActivityExample);
            if(!CollectionUtils.isEmpty(activities)){
                actVos=convertToActVo(activities);
                jedisService.hset(RedisKey.act.getKey(), alertWinLoc.toString(),gson.toJson(actVos));
            }
        }else{
            Type type = new TypeToken<List<ActVo>>(){}.getType();
            actVos = gson.fromJson(actListStr, type);
        }

        busiResult.setData(actVos);
        return busiResult;
    }
    private List<ActVo> convertToActVo(List<AppActivity> appActivities){
        List<ActVo> actVos= Lists.newArrayList();
        for(AppActivity appActivity:appActivities){
            ActVo actVo=new ActVo();
            actVo.setActId(appActivity.getActId());
            actVo.setActName(appActivity.getActName());
            actVo.setAlertWin(appActivity.getAlertWin().intValue()==1?true:false);
            actVo.setAlertWinLoc(appActivity.getAlertWinLoc());
            actVo.setAlertWinPic(appActivity.getAlertWinPic());
            actVo.setSkipType(appActivity.getSkipType());
            actVo.setActAlertVersion(appActivity.getActAlertVersion());
            actVo.setSkipUrl(appActivity.getSkipUrl());
            actVos.add(actVo);
        }
        return actVos;
    }

}
