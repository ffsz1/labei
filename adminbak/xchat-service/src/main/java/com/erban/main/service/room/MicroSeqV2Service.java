package com.erban.main.service.room;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.model.redis.MicroSeqModelV2;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.MicroSeqV2Vo;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.xchat.common.constant.Constant.DefMsgType.*;
import static com.xchat.common.constant.Constant.MicroHandleStatus.inmicro;
import static com.xchat.common.constant.Constant.MicroHandleStatus.invited;

/**
 * Created by liuguofu on 2017/5/29.
 */
@Service
public class MicroSeqV2Service {
    private static final Logger logger = LoggerFactory.getLogger(MicroSeqV2Service.class);
    @Autowired
    private JedisService jedisService;

    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;
    @Autowired
    private RoomService roomService;

    @Autowired
    private UsersService usersService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    private  Gson gson=new Gson();
    private static int firstMicroSeqNo=0;
    private static int invittingSeqNo=300;
    private static int maxMicroNo=5;

    /**
     * 房主邀请用户上麦
     * @param uid
     * @param invitedUid
     * @return
     */
    public BusiResult inviteByRoomOwner(Long uid,Long invitedUid) throws Exception{
        Room room=roomService.getRoomByUid(uid);
        boolean valid=room.getValid();
        if(!valid){
            return new BusiResult(BusiStatus.ROOMRCLOSED);
        }
        List<MicroSeqModelV2> microSeqModelV2List=getMicroSeqModelV2ListByUid(uid);
        MicroSeqModelV2 microSeqModelV2=queryMicroSeqModelByUid(microSeqModelV2List,invitedUid);
        if(microSeqModelV2==null){
            Users users=usersService.getUsersByUid(invitedUid);
            microSeqModelV2=new MicroSeqModelV2(users.getUid(),users.getNick(),users.getAvatar());
            microSeqModelV2.setUid(invitedUid);
            microSeqModelV2.setSeqNo(invittingSeqNo);
            microSeqModelV2.setStatus(invited);
            microSeqModelV2List.add(microSeqModelV2);
        }
        Long roomId=room.getRoomId();
        String fromAccid=uid.toString();
        List<MicroSeqV2Vo> microSeqV2VoList=convertMicroSeqModelToVo(microSeqModelV2List);
        Map<String,Object> data= Maps.newHashMap();
        data.put("uid",invitedUid);
        data.put("data",microSeqV2VoList);
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId,fromAccid,Micro,MicroInvite,data);
        saveMicroSeqCache(uid,microSeqModelV2List);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 用户同意上麦
     * @param uid
     * @param roomUid
     * @return
     */
    public BusiResult acceptToUpMicro(Long uid,Long roomUid) throws Exception{
        Room room=roomService.getRoomByUid(roomUid);
        boolean valid=room.getValid();
        if(!valid){
            return new BusiResult(BusiStatus.ROOMRCLOSED);
        }
        List<MicroSeqModelV2> microSeqModelV2List=getMicroSeqModelV2ListByUid(roomUid);
        if(countInMicroList(microSeqModelV2List)>=maxMicroNo){
            return new BusiResult(BusiStatus.MICRONUMLIMIT);
        }
        MicroSeqModelV2 microSeqModelV2=queryMicroSeqModelByUid(microSeqModelV2List,uid);
        if(microSeqModelV2==null){
            return new BusiResult(BusiStatus.MICRONOTININVIATEDLIST);
        }

        microSeqModelV2List=updateMicroSeqToTop(microSeqModelV2List,microSeqModelV2);
        Long roomId=room.getRoomId();
        String fromAccid=uid.toString();
        List<MicroSeqV2Vo> microSeqV2VoList=convertMicroSeqModelToVo(microSeqModelV2List);
        Map<String,Object> data= Maps.newHashMap();
        data.put("uid",uid);
        data.put("data",microSeqV2VoList);
        erBanNetEaseService.setChatRoomMemberRole(roomId, roomUid.toString(),uid.toString(), Constant.RoleOpt.admin,  "true");
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId,fromAccid,Micro,MicroAccept,data);
        saveMicroSeqCache(roomUid,microSeqModelV2List);
        return new BusiResult(BusiStatus.SUCCESS);
    }
    private int countInMicroList(List<MicroSeqModelV2> microSeqModelV2List){
        int inMicroNum=0;
        if(CollectionUtils.isEmpty(microSeqModelV2List)){
            return inMicroNum;
        }
        for(MicroSeqModelV2 microSeqModelV2:microSeqModelV2List){
            if(microSeqModelV2.getStatus()==Constant.MicroHandleStatus.inmicro){
                inMicroNum++;
            }
        }
        return inMicroNum;
    }

    /**
     * 用户同意上麦后，直接指定为首麦位，其余麦位则往后推移
     * @param microSeqList
     * @param curMicroSeqModel
     * @return
     */
    private List<MicroSeqModelV2> updateMicroSeqToTop(List<MicroSeqModelV2> microSeqList,MicroSeqModelV2 curMicroSeqModel){
        microSeqList.remove(curMicroSeqModel);
        List<MicroSeqModelV2> newMicroSeqList=Lists.newArrayList();
        for(MicroSeqModelV2 microSeqModel:microSeqList){
            if(microSeqModel.getStatus()==inmicro){
                int seqNo=microSeqModel.getSeqNo();
                seqNo=seqNo+1;
                microSeqModel.setSeqNo(seqNo);
            }
            newMicroSeqList.add(microSeqModel);
        }

        curMicroSeqModel.setSeqNo(firstMicroSeqNo);
        curMicroSeqModel.setStatus(inmicro);
        newMicroSeqList.add(curMicroSeqModel);
        return newMicroSeqList;
    }

    /**
     * 房主踢用户下麦
     * @param uid
     * @param kickedUid
     * @return
     * @throws Exception
     */
    public BusiResult kickUserDownMicro(Long uid,Long kickedUid)throws Exception{

        Room room=roomService.getRoomByUid(uid);
        boolean valid=room.getValid();
        if(!valid){
            return new BusiResult(BusiStatus.ROOMRCLOSED);
        }
        List<MicroSeqModelV2> microSeqModelV2List=getMicroSeqModelV2ListByUid(uid);
        MicroSeqModelV2 microSeqModelV2=queryMicroSeqModelByUid(microSeqModelV2List,kickedUid);
        if(microSeqModelV2==null){
            return new BusiResult(BusiStatus.SUCCESS);
        }
        microSeqModelV2List.remove(microSeqModelV2);
        Long roomId=room.getRoomId();
        String fromAccid=uid.toString();
        List<MicroSeqV2Vo> microSeqV2VoList=convertMicroSeqModelToVo(microSeqModelV2List);
        Map<String,Object> data= Maps.newHashMap();
        data.put("uid",kickedUid);
        data.put("data",microSeqV2VoList);
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId,fromAccid,Micro,MicroOwnerKickUserV2,data);
        erBanNetEaseService.setChatRoomMemberRole(roomId, uid.toString(),kickedUid.toString(), Constant.RoleOpt.admin,  "false");
        saveMicroSeqCache(uid,microSeqModelV2List);
        return new BusiResult(BusiStatus.SUCCESS);

    }

    /**
     * 置顶用户麦序
     * @param uid
     * @param topUid
     * @return
     * @throws Exception
     */
    public BusiResult topUserMicro(Long uid,Long topUid) throws Exception{
        Room room=roomService.getRoomByUid(uid);
        boolean valid=room.getValid();
        if(!valid){
            return new BusiResult(BusiStatus.ROOMRCLOSED);
        }
        List<MicroSeqModelV2> microSeqModelV2List=getMicroSeqModelV2ListByUid(uid);
        MicroSeqModelV2 microSeqModelV2=queryMicroSeqModelByUid(microSeqModelV2List,topUid);
        if(microSeqModelV2==null){
            return new BusiResult(BusiStatus.SUCCESS);
        }
        updateMicroSeqToTop(microSeqModelV2List,microSeqModelV2);
        Long roomId=room.getRoomId();
        String fromAccid=uid.toString();
        List<MicroSeqV2Vo> microSeqV2VoList=convertMicroSeqModelToVo(microSeqModelV2List);
        Map<String,Object> data= Maps.newHashMap();
        data.put("uid",topUid);
        data.put("data",microSeqV2VoList);
        sendChatRoomMsgService.sendSendChatRoomMsg(roomId,fromAccid,Micro,MicroOwnerTopUserV2,data);
        saveMicroSeqCache(uid,microSeqModelV2List);
        return new BusiResult(BusiStatus.SUCCESS);
    }


    /**
     * 房主关闭房间清理麦序
     * @param uid
     * @throws Exception
     */
    public void ownerRemoveMicroSeqByCloseRoom(Long uid) throws Exception{
        List<MicroSeqModelV2> microSeqModelList =getMicroSeqModelV2ListByUid(uid);
        if(CollectionUtils.isEmpty(microSeqModelList)){
            return;
        }
        Room room=roomService.getRoomByUid(uid);
        for(MicroSeqModelV2 microSeqModel:microSeqModelList){
            Long userId=microSeqModel.getUid();
            if(uid.equals(userId)){
                continue;
            }
            erBanNetEaseService.setChatRoomMemberRole(room.getRoomId(), uid.toString(),userId.toString(), Constant.RoleOpt.admin,  "false");
        }
        deleteMicroSeqListByOwner(uid);
    }

    /**
     * 用户离开麦序
     * @param uid
     * @param roomUid
     * @return
     * @throws Exception
     */
    public BusiResult userLeftMicroSeq(Long uid,Long roomUid) throws Exception{
        List<MicroSeqModelV2> microSeqListList =getMicroSeqModelV2ListByUid(roomUid);
        if(CollectionUtils.isEmpty(microSeqListList)){
            return new BusiResult(BusiStatus.SUCCESS);
        }
        MicroSeqModelV2 microSeqModelV2=queryMicroSeqModelByUid(microSeqListList,uid);
        if(!microSeqListList.remove(microSeqModelV2)){
            return new BusiResult(BusiStatus.SUCCESS);
        }
        Room room=roomService.getRoomByUid(roomUid);
        List<MicroSeqV2Vo> microSeqV2VoList=convertMicroSeqModelToVo(microSeqListList);
        Map<String,Object> data= Maps.newHashMap();
        data.put("uid",uid);
        data.put("data",microSeqV2VoList);
        sendChatRoomMsgService.sendSendChatRoomMsg(room.getRoomId(),uid.toString(), Constant.DefMsgType.Micro, Constant.DefMsgType.MicroUserLeftV2,data);
        erBanNetEaseService.setChatRoomMemberRole( room.getRoomId(), roomUid.toString(), uid.toString(), Constant.RoleOpt.admin,  "false");
        saveMicroSeqCache(roomUid,microSeqListList);
        return new BusiResult(BusiStatus.SUCCESS);

    }

    public BusiResult deleteMicroSeqListByOwner(Long uid) throws  Exception{
        jedisService.hdelete(RedisKey.microv2_list.getKey(),String.valueOf(uid),"");
        return new BusiResult(BusiStatus.SUCCESS);
    }


    private List<MicroSeqModelV2> getMicroSeqModelV2ListByUid(Long uid){
        String micorSeqV2ListStr= jedisService.hget(RedisKey.microv2_list.getKey(),uid.toString());
        if(StringUtils.isBlank(micorSeqV2ListStr)){
            return Lists.newArrayList();
        }
        Type type = new TypeToken<List<MicroSeqModelV2>>(){}.getType();
        List<MicroSeqModelV2> microSeqModelV2List = gson.fromJson(micorSeqV2ListStr, type);
        return microSeqModelV2List;

    }

    private void saveMicroSeqCache(Long uid,List<MicroSeqModelV2> microSeqModelList){
        if(microSeqModelList==null){
            microSeqModelList=Lists.newArrayList();
        }
        String microListStr=gson.toJson(microSeqModelList);
        jedisService.hwrite(RedisKey.microv2_list.getKey(),String.valueOf(uid),microListStr);
    }

    private MicroSeqModelV2 queryMicroSeqModelByUid(List<MicroSeqModelV2> seqModelList,Long uid){
        MicroSeqModelV2 curMicroSeqModel=null;
        for(MicroSeqModelV2 microSeqModel:seqModelList){
            if(uid.equals(microSeqModel.getUid())){
                curMicroSeqModel=microSeqModel;
                break;
            }
        }
        return curMicroSeqModel;
    }


    private List<MicroSeqV2Vo> convertMicroSeqModelToVo(List<MicroSeqModelV2> microSeqModels){
        List<MicroSeqV2Vo> microSeqV2VoList=Lists.newArrayList();
        if(CollectionUtils.isEmpty(microSeqModels)){
            return microSeqV2VoList;
        }
        for(MicroSeqModelV2 microSeqModelV2:microSeqModels){
            MicroSeqV2Vo microSeqV2Vo=new MicroSeqV2Vo();
            microSeqV2Vo.setUid(microSeqModelV2.getUid());
            microSeqV2Vo.setAvatar(microSeqModelV2.getAvatar());
            microSeqV2Vo.setNick(microSeqModelV2.getNick());
            microSeqV2Vo.setSeqNo(microSeqModelV2.getSeqNo());
            microSeqV2Vo.setStatus(microSeqModelV2.getStatus());
            microSeqV2VoList.add(microSeqV2Vo);
        }
        Collections.sort(microSeqV2VoList);
        return microSeqV2VoList;
    }


    /**获取房间当前麦序列表
     *
     * @param uid 房主uid
     * @return
     */
    public BusiResult getAllMicroList(Long uid){
        List<MicroSeqV2Vo> voList=convertMicroSeqModelToVo(getMicroSeqModelV2ListByUid(uid));
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(voList);
        return busiResult;
    }



}
