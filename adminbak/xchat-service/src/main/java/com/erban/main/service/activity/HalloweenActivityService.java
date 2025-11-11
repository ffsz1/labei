package com.erban.main.service.activity;

import com.erban.main.model.Users;
import com.erban.main.mybatismapper.HalloweenActivityMapperMgr;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.HalloweenActRankParentVo;
import com.erban.main.vo.HalloweenActRankVo;
import com.erban.main.vo.RankHomeVo;
import com.erban.main.vo.RankVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liuguofu on 2017/10/28.
 */
@Service
public class HalloweenActivityService {
    @Autowired
    private HalloweenActivityMapperMgr halloweenActivityMapperMgr;
    @Autowired
    private UsersService usersService;

    @Autowired
    private JedisService jedisService;
    private int[] type={1,2,3};

    public static int defaultSeqNo=909231;
    private Gson gson=new Gson();
    /**
     * 万圣节活动排行榜
     *
     * @param type 1是查富豪排行榜，2是查明星排行榜，3是房间榜
     * @param uid
     * @return
     */
    public BusiResult queryHalloweenActRank(Long uid,int type) {
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        HalloweenActRankParentVo halloweenActRankParentVo=null;
        if(type==1){
             halloweenActRankParentVo= queryRicherHalloweenActRank(uid);
        }else if(type==2){
             halloweenActRankParentVo= queryStarHalloweenActRank(uid);
        }else if(type==3){
            halloweenActRankParentVo=queryRoomHalloweenActRank(uid);
        }
        busiResult.setData(halloweenActRankParentVo);
        return busiResult;
    }

    public void  doHalloweenActRank(){
        List<HalloweenActRankVo> halloweenActRankVos1= halloweenActivityMapperMgr.queryRicherHalloweenActRank();
        List<HalloweenActRankVo> halloweenActRankVos2= halloweenActivityMapperMgr.queryStarHalloweenActRank();
        List<HalloweenActRankVo> halloweenActRankVos3= halloweenActivityMapperMgr.queryRoomHalloweenActRank();
        saveRankListVo(halloweenActRankVos1,1);
        saveRankListVo(halloweenActRankVos2,2);
        saveRankListVo(halloweenActRankVos3,3);
    }

    private HalloweenActRankParentVo queryRicherHalloweenActRank(Long uid){
        Users users=usersService.getUsersByUid(uid);
        if(users==null){
            return new HalloweenActRankParentVo();
        }
        HalloweenActRankParentVo halloweenActRankParentVo=new HalloweenActRankParentVo();

        List<HalloweenActRankVo> halloweenActRankVos=getActRankCache(1);
        if(CollectionUtils.isEmpty(halloweenActRankVos)){
            halloweenActRankVos= halloweenActivityMapperMgr.queryRicherHalloweenActRank();
        }

        halloweenActRankParentVo.setRankList(halloweenActRankVos);
//        HalloweenActRankVo mine=queryMyRicherHalloweenGoldNum(uid);
//        if(mine==null){
//            mine=new HalloweenActRankVo();
//            mine.setSeqNo(defaultSeqNo);
//            mine.setTotalCount(0);
//
//        }else{
//            int seqNo=queryMineRicherHalloweenActRank(mine.getTotalCount());
//            mine.setSeqNo(seqNo);
//        }
//        mine.setAvatar(users.getAvatar());
//        mine.setErbanNo(users.getErbanNo());
//        mine.setNick(users.getNick());
//        halloweenActRankParentVo.setMe(mine);
        return halloweenActRankParentVo;
    }

    private HalloweenActRankParentVo queryStarHalloweenActRank(Long uid){
        Users users=usersService.getUsersByUid(uid);
        if(users==null){
            return new HalloweenActRankParentVo();
        }
        HalloweenActRankParentVo halloweenActRankParentVo=new HalloweenActRankParentVo();
        List<HalloweenActRankVo> halloweenActRankVos=getActRankCache(2);
        if(CollectionUtils.isEmpty(halloweenActRankVos)){
            halloweenActRankVos= halloweenActivityMapperMgr.queryStarHalloweenActRank();
        }
        halloweenActRankParentVo.setRankList(halloweenActRankVos);
//        HalloweenActRankVo mine=queryMyStarHalloweenGoldNum(uid);
//        if(mine==null){
//            mine=new HalloweenActRankVo();
//            mine.setSeqNo(defaultSeqNo);
//            mine.setTotalCount(0);
//            mine.setNick(users.getNick());
//
//        }else{
//            int seqNo=queryMineStarHalloweenActRank(mine.getTotalCount());
//            mine.setSeqNo(seqNo);
//        }
//        mine.setAvatar(users.getAvatar());
//        mine.setErbanNo(users.getErbanNo());
//        mine.setNick(users.getNick());
//        halloweenActRankParentVo.setMe(mine);
        return halloweenActRankParentVo;
    }

    private HalloweenActRankParentVo queryRoomHalloweenActRank(Long uid){
        Users users=usersService.getUsersByUid(uid);
        if(users==null){
            return new HalloweenActRankParentVo();
        }
        HalloweenActRankParentVo halloweenActRankParentVo=new HalloweenActRankParentVo();

        List<HalloweenActRankVo> halloweenActRankVos=getActRankCache(3);
        if(CollectionUtils.isEmpty(halloweenActRankVos)){
            halloweenActRankVos= halloweenActivityMapperMgr.queryRoomHalloweenActRank();
        }
        halloweenActRankParentVo.setRankList(halloweenActRankVos);
//        HalloweenActRankVo mine=queryMyRoomHalloweenGoldNum(uid);
//        if(mine==null){
//            mine=new HalloweenActRankVo();
//            mine.setSeqNo(defaultSeqNo);
//            mine.setTotalCount(0);
//            mine.setNick(users.getNick());
//
//        }else{
//            int seqNo=queryMineRoomHalloweenActRank(mine.getTotalCount());
//            mine.setSeqNo(seqNo);
//        }
//        mine.setAvatar(users.getAvatar());
//        mine.setErbanNo(users.getErbanNo());
//        mine.setNick(users.getNick());
//        halloweenActRankParentVo.setMe(mine);
        return halloweenActRankParentVo;
    }

    // 1是查富豪排行榜，2是查明星排行榜，3是房间榜
    private List<HalloweenActRankVo> getActRankCache(int type){
        String rankVoListStr=jedisService.hget(RedisKey.act_rank.getKey(),String.valueOf(type));
        List<HalloweenActRankVo> rankVoList =null;
        if(StringUtils.isEmpty(rankVoListStr)){
            return null;
        }else{
            Type typeJson = new TypeToken<List<HalloweenActRankVo>>(){}.getType();
            rankVoList = gson.fromJson(rankVoListStr, typeJson);
        }
        return rankVoList;
    }
    private void  saveRankListVo(List<HalloweenActRankVo>rankVoList ,int type){
        if(rankVoList==null){
            return;
        }
        jedisService.hwrite(RedisKey.act_rank.getKey(),String.valueOf(type),gson.toJson(rankVoList));
    }




    private HalloweenActRankVo queryMyRicherHalloweenGoldNum(Long uid){
        HalloweenActRankVo halloweenActRankVo= halloweenActivityMapperMgr.queryMyRicherHalloweenGoldNum(uid);
        return halloweenActRankVo;
    }

    private HalloweenActRankVo  queryMyStarHalloweenGoldNum(Long uid){
        HalloweenActRankVo halloweenActRankVo= halloweenActivityMapperMgr.queryMyStarHalloweenGoldNum(uid);
        return halloweenActRankVo;
    }

    private int queryMineRicherHalloweenActRank(Long totalCount){
        int seqNo= halloweenActivityMapperMgr.queryMineRicherHalloweenActRank(totalCount);
        return seqNo;
    }

    private int  queryMineStarHalloweenActRank(Long totalCount){
        int seqNo= halloweenActivityMapperMgr.queryMineStarHalloweenActRank(totalCount);
        return seqNo;
    }

    private HalloweenActRankVo  queryMyRoomHalloweenGoldNum(Long uid){
        HalloweenActRankVo halloweenActRankVo= halloweenActivityMapperMgr.queryMyRoomHalloweenGoldNum(uid);
        return halloweenActRankVo;
    }

    private int  queryMineRoomHalloweenActRank(Long totalCount){
        int seqNo= halloweenActivityMapperMgr.queryMineRoomHalloweenActRank(totalCount);
        return seqNo;
    }






}
