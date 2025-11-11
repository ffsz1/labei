package com.erban.main.service.room;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Sets;
import com.erban.main.model.Room;
import com.erban.main.model.redis.MicroSeqModel;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.vo.MicroSeqListVo;
import com.erban.main.vo.SeqVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by liuguofu on 2017/5/29.
 */
@Service
public class MicroSeqService {
    private static final Logger logger = LoggerFactory.getLogger(MicroSeqService.class);
    @Autowired
    private JedisService jedisService;
    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    private  Gson gson=new Gson();
    private static int firstMicroSeqNo=0;

    public BusiResult upMicro(Long uid, Long roomUid, int seqNo) throws Exception{
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        Room room=roomService.getRoomByUid(roomUid);
        List<MicroSeqModel> microSeqListList =getMicroSeqListCache(roomUid);
        if(CollectionUtils.isEmpty(microSeqListList)){
            microSeqListList=Lists.newArrayList();
        }else{
            microSeqListList=removeMicroSeqModelByUid(microSeqListList,uid);
            for(MicroSeqModel microSeqModel:microSeqListList){
                int microSeqNo=microSeqModel.getSeqNo();
                if(seqNo==microSeqNo){//麦位上有人了
                    busiResult.setCode(BusiStatus.BUSIERROR.value());
                    busiResult.setMessage("当前麦上有人，不能上麦！");
                    return busiResult;
                }
            }
        }
        MicroSeqModel microSeqModel=new MicroSeqModel();
        microSeqModel.setUid(uid);
        microSeqModel.setSeqNo(seqNo);
        microSeqListList.add(microSeqModel);
        saveMicroSeqCache(roomUid,microSeqListList);
        Map<String,Object> data=Maps.newHashMap();
        data.put("uid",uid);
        data.put("data",getMicroVos(roomUid));
        if(!uid.equals(roomUid)){
            erBanNetEaseService.setChatRoomMemberRole( room.getRoomId(), roomUid.toString(), uid.toString(), Constant.RoleOpt.admin,  "true");
        }
        sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(),uid.toString(), Constant.DefMsgType.Micro, Constant.DefMsgType.MicroUp,data);
        return busiResult;
    }

    public void ownerRemoveMicroSeqByLeftRoom(Long uid) throws Exception{
        List<MicroSeqModel> microSeqModelList =getMicroSeqListCache(uid);
        if(CollectionUtils.isEmpty(microSeqModelList)){
            return;
        }
        Room room=roomService.getRoomByUid(uid);
        for(MicroSeqModel microSeqModel:microSeqModelList){
            Long userId=microSeqModel.getUid();
            if(uid.equals(userId)){
                continue;
            }
            erBanNetEaseService.setChatRoomMemberRole( room.getRoomId(), uid.toString(),userId.toString(), Constant.RoleOpt.admin,  "false");
        }
        deleteMicroSeqListByOwner(uid);
    }

    public BusiResult userLeftMicroSeq(Long uid,Long roomUid) throws Exception{
        List<MicroSeqModel> microSeqListList =getMicroSeqListCache(roomUid);
        if(CollectionUtils.isEmpty(microSeqListList)){
            return new BusiResult(BusiStatus.SUCCESS);
        }
        List<MicroSeqModel> microSeqModelListModify=Lists.newArrayList();
        for(MicroSeqModel seqModel:microSeqListList){
            if(seqModel.getUid().equals(uid)){
                continue;
            }
            microSeqModelListModify.add(seqModel);
        }
        Room room=roomService.getRoomByUid(roomUid);
        saveMicroSeqCache(roomUid,microSeqModelListModify);
        Map<String,Object> data=Maps.newHashMap();
        data.put("uid",uid);
        data.put("data",getMicroVos(roomUid));
        sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(),uid.toString(), Constant.DefMsgType.Micro, Constant.DefMsgType.MicroDown,data);
        erBanNetEaseService.setChatRoomMemberRole( room.getRoomId(), roomUid.toString(), uid.toString(), Constant.RoleOpt.admin,  "false");
        return new BusiResult(BusiStatus.SUCCESS);

}

    public BusiResult deleteMicroSeqListByOwner(Long uid) throws  Exception{
        jedisService.hwrite(RedisKey.micro_seq.getKey(),String.valueOf(uid),"");
        return new BusiResult(BusiStatus.SUCCESS);
    }
    private List<MicroSeqModel> getMicroSeqListCache(Long uid){
        String microSeqStr=jedisService.hget(RedisKey.micro_seq.getKey(),String.valueOf(uid));
        Type type = new TypeToken<List<MicroSeqModel>>(){}.getType();
        List<MicroSeqModel> microSeqListVoList = gson.fromJson(microSeqStr, type);
        return microSeqListVoList;
    }
    private void saveMicroSeqCache(Long uid,List<MicroSeqModel> microSeqModelList){
        if(microSeqModelList==null){
            microSeqModelList=Lists.newArrayList();
        }
        String microListStr=gson.toJson(microSeqModelList);
        jedisService.hwrite(RedisKey.micro_seq.getKey(),String.valueOf(uid),microListStr);
    }

    public BusiResult getMicroVo(Long uid) throws Exception{
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        MicroSeqListVo microSeqListVo=getMicroVos(uid);
        busiResult.setData(microSeqListVo);
        return busiResult;
    }

    private MicroSeqListVo getMicroVos(Long uid) throws Exception{
        List<MicroSeqModel> microSeqListVoList=getMicroSeqListCache(uid);
        List<SeqVo> seqVos=convertMicroSeqModelToVo(microSeqListVoList);
        MicroSeqListVo microSeqListVo=new MicroSeqListVo();
        microSeqListVo.setUid(uid);
        microSeqListVo.setSeqUids(seqVos);

        return microSeqListVo;
    }

    public BusiResult updateMicroSeqByRoomOwner(Long uid,String curUids,int type) throws Exception{
//        seqUids=90005_1,90006_2,90007_4
        //45房主给用户上麦，46房主踢用户下麦，47置顶用户麦序
        Room room=roomService.getRoomByUid(uid);
        List<MicroSeqModel> microSeqList=null;
        if(type==Constant.DefMsgType.MicroOwnerUpUser){
            microSeqList=ownerUpUserMicroSeq(uid,curUids);
            erBanNetEaseService.setChatRoomMemberRole(room.getRoomId(), uid.toString(), curUids, Constant.RoleOpt.admin,  "false");
        }else if(type==Constant.DefMsgType.MicroOwnerExchange){
            microSeqList=ownerExchangeMicroSeq(uid,curUids);
        }else if(type==Constant.DefMsgType.MicroOwnerKickUser){
            microSeqList=ownerKickMicroSeq(uid,curUids);
            erBanNetEaseService.setChatRoomMemberRole(room.getRoomId(), uid.toString(), curUids, Constant.RoleOpt.admin,  "true");
        }else if(type==Constant.DefMsgType.MicroOwnerTopUser){
            microSeqList=ownerTopMicroSeq(uid,curUids);
        }else{
            return new BusiResult(BusiStatus.BUSIERROR,"不支持的操作类型");

        }
        saveMicroSeqCache(uid,microSeqList);
        Object objData;
        Map<String,Object> objDataMap=Maps.newHashMap();
        objDataMap.put("uid",curUids);
        objDataMap.put("data",getMicroVos(uid));
        objData=objDataMap;
        sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(),uid.toString(),Constant.DefMsgType.Micro,type,objData);
        BusiResult busiResult=getMicroVo(uid);
        return  busiResult;
    }


    private List<MicroSeqModel> ownerUpUserMicroSeq(Long uid,String curUids){
        List<MicroSeqModel> microSeqList=getMicroSeqListCache(uid);
        Long curUid=Long.valueOf(curUids);
        if(CollectionUtils.isEmpty(microSeqList)){
            MicroSeqModel microSeqModel=new MicroSeqModel();
            microSeqModel.setSeqNo(firstMicroSeqNo);
            microSeqModel.setUid(curUid);
            microSeqList.add(microSeqModel);
            microSeqList=Lists.newArrayList();
            return microSeqList;
        }
        int noneSeqNo=-1;
        for(int seqNo=firstMicroSeqNo;seqNo<6;seqNo++){
            boolean result=false;
            for(MicroSeqModel microSeqModel:microSeqList){
                if(seqNo==microSeqModel.getSeqNo()){
                    result=false;
                    break;
                }else{
                    result=true;
                }
            }
            if(result){
                noneSeqNo=seqNo;
                break;
            }
        }
        if(noneSeqNo==-1){
            return microSeqList;
        }else{
            MicroSeqModel microSeqModel=new MicroSeqModel();
            microSeqModel.setUid(curUid);
            microSeqModel.setSeqNo(noneSeqNo);
            microSeqList.add(microSeqModel);
        }

        return microSeqList;

    }
    private List<MicroSeqModel> ownerKickMicroSeq(Long uid,String curUids){
        List<MicroSeqModel> microSeqList=getMicroSeqListCache(uid);
        if(CollectionUtils.isEmpty(microSeqList)){
            microSeqList=Lists.newArrayList();
            return microSeqList;
        }
        microSeqList=removeMicroSeqModelByUid(microSeqList,Long.valueOf(curUids));
        return microSeqList;

    }
    private List<MicroSeqModel> ownerExchangeMicroSeq(Long uid,String curUids){
        List<MicroSeqModel> microSeqList=getMicroSeqListCache(uid);
        if(CollectionUtils.isEmpty(microSeqList)){
            microSeqList=Lists.newArrayList();
            return microSeqList;
        }
        String curUidsArray[]=curUids.split(",");
        Long uid0=Long.valueOf(curUidsArray[0]);
        Long uid1=Long.valueOf(curUidsArray[1]);
        MicroSeqModel microSeqModel0=getMicroSeqModelByUid(microSeqList,uid0);
        MicroSeqModel microSeqModel1=getMicroSeqModelByUid(microSeqList,uid1);
        microSeqList=removeMicroSeqModelByUid(microSeqList,uid0);
        microSeqList=removeMicroSeqModelByUid(microSeqList,uid1);
        int seqNo0=microSeqModel0.getSeqNo();
        int seqNo1=microSeqModel1.getSeqNo();
        int exchangeSeqNo;
        exchangeSeqNo=seqNo0;
        seqNo0=seqNo1;
        seqNo1=exchangeSeqNo;
        microSeqModel0.setSeqNo(seqNo0);
        microSeqModel1.setSeqNo(seqNo1);
        microSeqList.add(microSeqModel0);
        microSeqList.add(microSeqModel1);
        return microSeqList;

    }
    private List<MicroSeqModel> ownerTopMicroSeq(Long uid,String curUids){
        Long curUid=Long.valueOf(curUids);
        List<MicroSeqModel> microSeqList=getMicroSeqListCache(uid);
        if(CollectionUtils.isEmpty(microSeqList)){
            MicroSeqModel microSeqModel0=new MicroSeqModel();
            microSeqModel0.setUid(curUid);
            microSeqModel0.setSeqNo(firstMicroSeqNo);
            microSeqList=Lists.newArrayList();
            microSeqList.add(microSeqModel0);
            return microSeqList;
        }
        MicroSeqModel curMicroSeqModel=getMicroSeqModelByUid(microSeqList,curUid);
        if(curMicroSeqModel==null){
            return microSeqList;
        }

        microSeqList=removeMicroSeqModelByUid(microSeqList,curUid);
        List<MicroSeqModel> newMicroSeqList=Lists.newArrayList();
        for(MicroSeqModel microSeqModel:microSeqList){
            int seqNoCur=curMicroSeqModel.getSeqNo();
            int seqNo=microSeqModel.getSeqNo();
            if(seqNo<seqNoCur){
                int exchangeSeqNo;
                exchangeSeqNo=seqNoCur;
                seqNoCur=seqNo;
                seqNo=exchangeSeqNo;
                microSeqModel.setSeqNo(seqNo);
                curMicroSeqModel.setSeqNo(seqNoCur);
            }
            newMicroSeqList.add(microSeqModel);
        }
        if(curMicroSeqModel.getSeqNo()==firstMicroSeqNo){
            newMicroSeqList.add(curMicroSeqModel);
        }else{
            curMicroSeqModel.setSeqNo(firstMicroSeqNo);
            newMicroSeqList.add(curMicroSeqModel);
        }
        return newMicroSeqList;
    }

    private List<MicroSeqModel> removeMicroSeqModelByUid(List<MicroSeqModel> seqModelList,Long removeUid){
        List<MicroSeqModel> newMicroSeqModelList=Lists.newArrayList();
        for(MicroSeqModel microSeqModel:seqModelList){
            if(removeUid.equals(microSeqModel.getUid())){
                continue;
            }
            newMicroSeqModelList.add(microSeqModel);
        }
        return newMicroSeqModelList;
    }
    private MicroSeqModel getMicroSeqModelByUid(List<MicroSeqModel> seqModelList,Long uid){
        MicroSeqModel curMicroSeqModel=null;
        for(MicroSeqModel microSeqModel:seqModelList){
            if(uid.equals(microSeqModel.getUid())){
                curMicroSeqModel=microSeqModel;
                break;
            }
        }
        return curMicroSeqModel;
    }

    private List<SeqVo> convertMicroSeqModelToVo(List<MicroSeqModel> microSeqModels){
        List<SeqVo> seqVos=Lists.newArrayList();
        if(CollectionUtils.isEmpty(microSeqModels)){
            return seqVos;
        }
        for(MicroSeqModel microSeqModel:microSeqModels){
            SeqVo seqVo=new SeqVo();
            seqVo.setUid(microSeqModel.getUid());
            seqVo.setSeqNo(microSeqModel.getSeqNo());
            seqVos.add(seqVo);
        }
        return seqVos;
    }

    public static void main(String args[]){
        MicroSeqService MicroSeqService=new MicroSeqService();
//        MicroSeqService.getMicroSeqListCache(0L);
        MicroSeqListVo seqListVo=new MicroSeqListVo();
        Set<MicroSeqModel> sets=Sets.newHashSet();
        MicroSeqModel m1=new MicroSeqModel();
        m1.setSeqNo(1);
        m1.setUid(1L);
        sets.add(m1);
        MicroSeqModel m2=new MicroSeqModel();
        m2.setSeqNo(1);
        m2.setUid(1L);
        sets.add(m2);
        System.out.println(sets.size());




    }



}
