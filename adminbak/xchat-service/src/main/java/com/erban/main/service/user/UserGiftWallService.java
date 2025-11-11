package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.UserGiftWall;
import com.erban.main.mybatismapper.UserGiftWallMapper;
import com.erban.main.mybatismapper.UserGiftWallMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/10/17.
 */
@Service
public class UserGiftWallService extends BaseService {
    @Autowired
    private UserGiftWallMapperMgr userGiftWallMapperMgr;
    @Autowired
    private UserGiftWallMapper userGiftWallMapper;
    @Autowired
    private UsersService usersService;

    public BusiResult< List<UserGiftWall>> getUserWallListByUid(Long uid,int orderType){
        BusiResult<List<UserGiftWall>> busiResult=new BusiResult<>(BusiStatus.SUCCESS);
        List<UserGiftWall> userGiftWallList= Lists.newArrayList();
        if(orderType==1){//收到的礼物数量多少排序
            userGiftWallList=userGiftWallMapperMgr.getUserWallListByUidOrderByCount(uid);
        }else if(orderType==2){//礼物价格高低排序
           userGiftWallList=userGiftWallMapperMgr.getUserWallListByUidOrderByGoldPrice(uid);
        }
        userGiftWallList=mergeGiftOfNanguantang(userGiftWallList);
        busiResult.setData(userGiftWallList);
        return busiResult;
    }

    private List<UserGiftWall>  mergeGiftOfNanguantang(List<UserGiftWall> userGiftWallList){
        if(CollectionUtils.isEmpty(userGiftWallList)){
            return userGiftWallList;
        }
        List<UserGiftWall> wholeUserGiftWall =Lists.newArrayList();
        //取出含有南瓜糖的礼物
        List<UserGiftWall> userGiftWallListNangua=Lists.newArrayList();
        List<UserGiftWall> userGiftWallListOthers=Lists.newArrayList();
        for(UserGiftWall userGiftWall:userGiftWallList){
            int giftId=userGiftWall.getGiftId();
            if(giftId==1025||giftId==1026){
                userGiftWallListNangua.add(userGiftWall);
            }else{
                userGiftWallListOthers.add(userGiftWall);
            }
        }
        if(userGiftWallListNangua.size()<=1){
            return userGiftWallList;
        }
        UserGiftWall userGiftWallNanguaTotal=new UserGiftWall();
        int nanguaTotalCount=0;
        for(UserGiftWall userGiftWallNangua:userGiftWallListNangua){
            nanguaTotalCount=nanguaTotalCount+userGiftWallNangua.getReciveCount();
            userGiftWallNanguaTotal=userGiftWallNangua;
            userGiftWallNanguaTotal.setReciveCount(nanguaTotalCount);
        }
        wholeUserGiftWall.add(userGiftWallNanguaTotal);
        wholeUserGiftWall.addAll(userGiftWallListOthers);
        return wholeUserGiftWall;

    }

    public boolean updateGiftWallCount(Long uid,Integer giftId,int giftNum){
        UserGiftWall userGiftWall=new UserGiftWall();
        userGiftWall.setGiftId(giftId);
        userGiftWall.setUid(uid);
        userGiftWall.setReciveCount(giftNum);
        int updateCount=userGiftWallMapperMgr.updateGiftWallCount(userGiftWall);
        if(updateCount==0){
            insertUserGiftWall(uid,giftId,giftNum);
        }
        return true;
    }

    private void insertUserGiftWall(Long uid,Integer giftId,int giftNum){
        UserGiftWall userGiftWall=new UserGiftWall();
        userGiftWall.setUid(uid);
        userGiftWall.setGiftId(giftId);
        userGiftWall.setReciveCount(giftNum);
        userGiftWall.setCreateTime(new Date());
        userGiftWallMapper.insert(userGiftWall);
    }

    /***
     *  刷新礼物墙满的用户
     */
    public void refreshFullGiftWallUser(){
        String result = jdbcTemplate.queryForObject("select GROUP_CONCAT(a.uid) from (select ugw.uid from user_gift_wall ugw INNER JOIN gift g on ugw.gift_id = g.gift_id GROUP BY ugw.uid HAVING COUNT(1) > 14 ORDER BY SUM(ugw.recive_count*g.gold_price) DESC) a", String.class);
        if(StringUtils.isEmpty(result)){
            jedisService.set(RedisKey.full_gift_wall_user.getKey(), "");
        }else{
            jedisService.set(RedisKey.full_gift_wall_user.getKey(), result);
        }
    }

    public BusiResult getFullUserList(Integer pageSize){
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String result = jedisService.get(RedisKey.full_gift_wall_user.getKey());
        if(StringUtils.isEmpty(result)){
            busiResult.setData(new ArrayList<>());
        }else{
            List<UserVo> userVoList = new ArrayList<>();
            String[] list = result.split(",");
            if(pageSize==null){
                pageSize=list.length;
            }else if(pageSize>list.length){
                pageSize=list.length;
            }
            for(int i=0;i<pageSize;i++){
                userVoList.add(usersService.getUserVoByUid(Long.valueOf(list[i])));
            }
            busiResult.setData(userVoList);
        }
        return busiResult;
    }

}
