package com.erban.main.service.statis;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatPacketActivityMapper;
import com.erban.main.mybatismapper.UserPacketMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UserConfigureService;
import com.erban.main.service.user.UserPacketService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.*;
import com.google.common.collect.Lists;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class StatPacketActivityService extends BaseService {
    @Autowired
    private StatPacketActivityMapper statPacketActivityMapper;
    @Autowired
    private UserPacketService userPacketService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPacketMapperMgr userPacketMapperMgr;
    @Autowired
    private UserConfigureService userConfigureService;
    private static DecimalFormat doubleFormat = new DecimalFormat("0.00");

    /**
     * 更新statpacketactivty红包活动统计表
     */
    public void updateStatPacketAcitivtyByShare(Long uid){
        StatPacketActivity statPacketActivity=statPacketActivityMapper.selectByPrimaryKey(uid);
        if(statPacketActivity==null){
            statPacketActivity=getStatPacketActivityByUid(uid);
        }
        Date date=new Date();
        int shareCount=statPacketActivity.getShareCount();
        shareCount++;
        int packetCount=statPacketActivity.getPacketCount();
        packetCount++;
        int sharePacketCount=statPacketActivity.getSharePacketCount();
        sharePacketCount++;
        statPacketActivity.setUid(uid);
        statPacketActivity.setShareCount(shareCount);
        statPacketActivity.setPacketCount(packetCount);
        statPacketActivity.setSharePacketCount(sharePacketCount);
        statPacketActivity.setLatestShareDate(date);
        statPacketActivityMapper.updateByPrimaryKeySelective(statPacketActivity);

    }
    public void updateStatPacketActivityByRegister(Long uid){
        StatPacketActivity statPacketActivity=statPacketActivityMapper.selectByPrimaryKey(uid);
        if(statPacketActivity==null){
            statPacketActivity=getStatPacketActivityByUid(uid);
        }
        Date date=new Date();
        int registerCout=statPacketActivity.getRegisterCout();
        registerCout++;
        int packetCount=statPacketActivity.getPacketCount();
        packetCount++;
        int todayRegisterCount=statPacketActivity.getTodayRegisterCount();

        Date latestRegisterDate=statPacketActivity.getLatestRegisterDate();
        if(DateTimeUtil.checkIsToday(latestRegisterDate)){//今日新增注册，如果最近的一次注册日期是今天，则今日注册数量+1，如果不是，则今天注册数量为1
            todayRegisterCount++;
        }else{
            todayRegisterCount=1;
        }
        statPacketActivity.setUid(uid);
        statPacketActivity.setRegisterCout(registerCout);
        statPacketActivity.setTodayRegisterCount(todayRegisterCount);
        statPacketActivity.setPacketCount(packetCount);
        statPacketActivity.setLatestRegisterDate(date);
        statPacketActivityMapper.updateByPrimaryKeySelective(statPacketActivity);
    }

    /**
     * 充值分成奖励
     * @param uid 用户uid
     * @param chargeBouns  充值分成
     */
    public void updateStatPacketActivityByBouns(Long uid,double chargeBouns){
        StatPacketActivity statPacketActivity=statPacketActivityMapper.selectByPrimaryKey(uid);
        if(statPacketActivity==null){
            statPacketActivity=getStatPacketActivityByUid(uid);
        }
        Date date=new Date();
        statPacketActivity.setUid(uid);
        Double chargeBounsDb= statPacketActivity.getChargeBonus()==null?0:statPacketActivity.getChargeBonus();
        statPacketActivity.setChargeBonus(chargeBouns+chargeBounsDb);
        statPacketActivity.setUpdateTime(date);
        Date latestChargeBonusDate=statPacketActivity.getLatestChargeBonusDate();
        Double todayChargeBonus=statPacketActivity.getTodayChargeBonus();
        if(todayChargeBonus==null){
            todayChargeBonus=0.0;
        }
        if(DateTimeUtil.checkIsToday(latestChargeBonusDate)){//今日新增分成
            todayChargeBonus=todayChargeBonus+chargeBouns;
        }else{
            todayChargeBonus=chargeBouns;//今日首笔分成
        }
        statPacketActivity.setTodayChargeBonus(todayChargeBonus);
        statPacketActivity.setLatestChargeBonusDate(date);
        statPacketActivityMapper.updateByPrimaryKeySelective(statPacketActivity);
    }

    public StatPacketActivity getStatPacketActivityByUid(Long uid){
        StatPacketActivity statPacketActivity=statPacketActivityMapper.selectByPrimaryKey(uid);
        if(statPacketActivity==null){
            statPacketActivity=insertStatPacketActivityByFirstPacket(uid, Constant.PacketConst.fistrtPacketNum);
        }
        if(statPacketActivity.getLatestChargeBonusDate()!=null&&!DateTimeUtil.checkIsToday(statPacketActivity.getLatestChargeBonusDate())){
            statPacketActivity.setTodayChargeBonus(0.00);
        }
        if(statPacketActivity.getLatestRegisterDate()!=null&&!DateTimeUtil.checkIsToday(statPacketActivity.getLatestRegisterDate())){
            statPacketActivity.setTodayRegisterCount(0);
        }
        return  statPacketActivity;
    }
    public  BusiResult<StatPacketActivityVo> queryStatPacketVo(Long uid) throws Exception{
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        StatPacketActivityVo statPacketActivityVo =getStatPacketActivityVo(uid);
        busiResult.setData(statPacketActivityVo);
        return busiResult;
    }

    private StatPacketActivityVo getStatPacketActivityVo(Long uid){
        UserPacket userPacket=userPacketService.getUserPacket(uid);
        StatPacketActivity statPacketActivity=getStatPacketActivityByUid(uid);
        StatPacketActivityVo statPacketActivityVo =new StatPacketActivityVo();
        statPacketActivityVo.setUid(uid);
        statPacketActivityVo.setShareCount(statPacketActivity.getShareCount());
        statPacketActivityVo.setPacketNum(userPacket.getPacketNum());
        statPacketActivityVo.setPacketCount(statPacketActivity.getPacketCount());
        statPacketActivityVo.setRegisterCout(statPacketActivity.getRegisterCout());
        statPacketActivityVo.setChargeBonus(statPacketActivity.getChargeBonus());
        statPacketActivityVo.setTodayRegisterCount(statPacketActivity.getTodayRegisterCount());
        return statPacketActivityVo;
    }
    public StatPacketActRankVo getStatPacketActRankVo(Long uid){

        UserPacket userPacket=userPacketService.getUserPacket(uid);

        StatPacketActRankVo statPacketActRankVo=new StatPacketActRankVo();

        statPacketActRankVo.setUid(uid);

        Users user=usersService.getUsersByUid(uid);
        if(user==null){
            return statPacketActRankVo;
        }
        statPacketActRankVo.setUid(uid);
        statPacketActRankVo.setNick(user.getNick());
        statPacketActRankVo.setAvatar(user.getAvatar());
        statPacketActRankVo.setPacketNum(userPacket.getPacketNum());
        statPacketActRankVo.setSeqNo(getStatPacketRankSeqNoByPacketNum(userPacket.getHistPacketNum()));// 改成历史金额的排行，下面列表是显示历史金额
        return statPacketActRankVo;
    }

    public int getStatPacketRankSeqNoByPacketNum(double packetNum){
        int rankNo=userPacketMapperMgr.getStatPacketRankSeqNoByPacketNum(packetNum);
        return rankNo;
    }

    public BusiResult<StatPacketActivityParentVo> queryStatPacketActivityRankList(Long uid){
        StatPacketActivityParentVo statPacketActivityParentVo = new StatPacketActivityParentVo();

        StatPacketActRankVo statPacketActivityVoMe= getStatPacketActRankVo(uid);

        statPacketActivityParentVo.setMe(statPacketActivityVoMe);

        List<UserPacket> userPacketList=queryUserPacketListBy();
        List<StatPacketActRankVo> rankList=getStatPacketActRankVoList(userPacketList);
        statPacketActivityParentVo.setRankList(rankList);
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(statPacketActivityParentVo);
        return busiResult;
    }

    /**
     * 获取我的邀请详情+明细
     * @param uid
     * @return
     */
    public BusiResult<StatPacketInviteDetailParentVo> getInviteDetail(Long uid){
        StatPacketInviteDetailParentVo statPacketInviteDetailParentVo=new StatPacketInviteDetailParentVo();
        //获取邀请详情
        StatPacketActivityVo statPacketActivityVo=getStatPacketActivityVo(uid);
        StatPacketInviteDetailVo inviteDetail=new StatPacketInviteDetailVo();
        inviteDetail.setUid(uid);
        inviteDetail.setTotalRegisterCount(statPacketActivityVo.getRegisterCout());
        inviteDetail.setPacketNum(statPacketActivityVo.getPacketNum());
        inviteDetail.setTodayRegisterCount(statPacketActivityVo.getTodayRegisterCount());
        UserConfigure userConfigure = userConfigureService.getOneByJedisId(uid.toString());
        inviteDetail.setDirectlyNum(statPacketActivityVo.getRegisterCout());
        //是否有上级分成权限
        if(userConfigure==null||userConfigure.getSuperiorBouns().intValue()==0){
            inviteDetail.setSuperiorBouns(new Byte("0"));
            inviteDetail.setLowerNum(0);
        }else{
            inviteDetail.setSuperiorBouns(userConfigure.getSuperiorBouns());
            inviteDetail.setLowerNum(jdbcTemplate.queryForObject("select COUNT(1) from stat_packet_register where uid in (select register_uid from stat_packet_register where uid = ?)", Integer.class, uid));
            inviteDetail.setTotalRegisterCount(inviteDetail.getDirectlyNum()+inviteDetail.getLowerNum());
        }
        //我的邀请明细
        List<StatPacketInviteRegisterListVo> inviteList=queryStatPacketInviteRegisterMap(uid);
        double totalInviteNum=0.0;
        if(!CollectionUtils.isEmpty(inviteList)){
            for(StatPacketInviteRegisterListVo StatPacketInviteRegisterListVo:inviteList){
                totalInviteNum+=StatPacketInviteRegisterListVo.getPacketNum();
            }
        }
        totalInviteNum=new Double(doubleFormat.format(totalInviteNum));
        inviteDetail.setPacketNum(totalInviteNum);
        statPacketInviteDetailParentVo.setInviteDetail(inviteDetail);
        statPacketInviteDetailParentVo.setInviteList(inviteList);
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(statPacketInviteDetailParentVo);
        return busiResult;
    }

    /**
     * 获取我的团队
     * @param uid
     */
    public BusiResult getTeam(Long uid){
        return new BusiResult(BusiStatus.SUCCESS, jdbcTemplate.queryForList("select u.uid, u.nick, u.avatar, date_format(a.create_time,'%Y-%m-%d') as createTime, (select COUNT(1) from stat_packet_register b where b.uid = a.register_uid) as invitationNum from stat_packet_register a INNER JOIN users u on a.register_uid = u.uid where a.uid = ?", uid));
    }

    public BusiResult<List<StatPacketActivity>> getSomeInviteDetail(String uidList){
        List<String> oldList = Arrays.asList(uidList.split(","));
        List<Long> newList = new ArrayList<Long>(oldList.size());
        List<StatPacketActivity> statPacketActivityList = Lists.newArrayList();
        for(String myString: oldList){
            newList.add(Long.valueOf(myString));
        }
        StatPacketActivityExample statPacketActivityExample = new StatPacketActivityExample();
        statPacketActivityExample.createCriteria().andUidIn(newList);
        statPacketActivityExample.setOrderByClause("uid");
        statPacketActivityList = statPacketActivityMapper.selectByExample(statPacketActivityExample);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(statPacketActivityList);
        return busiResult;
    }

    private List<StatPacketInviteRegisterListVo> queryStatPacketInviteRegisterMap(Long uid){
        List<StatPacketInviteRegisterListVo> statPacketInviteRegisterMapList =userPacketMapperMgr.queryStatPacketInviteRegisterMapList(uid);
        return statPacketInviteRegisterMapList;
    }


    private List<UserPacket> queryUserPacketListBy(){
        List<UserPacket> userPacketList=userPacketMapperMgr.queryUserPacketListBy();
        return userPacketList;
    }

    private List<StatPacketActRankVo> getStatPacketActRankVoList(List<UserPacket> userPacketList){
        if(CollectionUtils.isEmpty(userPacketList)){
            return Lists.newArrayList();
        }
        List<StatPacketActRankVo> statPacketActRankVoList=Lists.newArrayList();
        for(int i=0;i<userPacketList.size();i++){
            UserPacket userPacket=userPacketList.get(i);

            StatPacketActRankVo statPacketActRankVo=new StatPacketActRankVo();

            statPacketActRankVo.setUid(userPacket.getUid());

            Users user=usersService.getUsersByUid(userPacket.getUid());
            if(user==null){
                continue;
            }
            statPacketActRankVo.setUid(userPacket.getUid());
            statPacketActRankVo.setNick(user.getNick());
            statPacketActRankVo.setAvatar(user.getAvatar());
            statPacketActRankVo.setPacketNum(userPacket.getHistPacketNum());//排行榜展现历史红包数据
            statPacketActRankVo.setSeqNo(i+1);
            statPacketActRankVoList.add(statPacketActRankVo);
        }
        return statPacketActRankVoList;
    }

    public StatPacketActivity insertStatPacketActivityByFirstPacket(Long uid,double registerPacketNum){
        StatPacketActivity statPacketActivity=new StatPacketActivity();
        statPacketActivity.setUid(uid);
        statPacketActivity.setShareCount(0);
        statPacketActivity.setSharePacketCount(0);
        statPacketActivity.setLatestShareDate(null);
        statPacketActivity.setPacketCount(0);
        statPacketActivity.setRegisterCout(0);
        statPacketActivity.setTodayRegisterCount(0);
        statPacketActivity.setLatestRegisterDate(null);
        statPacketActivity.setChargeBonus(0.00);
        statPacketActivity.setTodayRegisterCount(0);
        statPacketActivity.setLatestChargeBonusDate(null);
        statPacketActivity.setCreateTime(new Date());
        statPacketActivityMapper.insert(statPacketActivity);
        return statPacketActivity;
    }

    public BusiResult queryStatBounsDetail(Long uid) {
        StatPacketBounsDetailParentVo statPacketBounsDetailParentVo=new StatPacketBounsDetailParentVo();
        StatPacketActivity statPacketActivity=getStatPacketActivityByUid(uid);

        List<StatPacketBounsListVo> statPacketBounsListVo=userPacketMapperMgr.queryInviteBonusList(uid);
        statPacketBounsDetailParentVo.setBounsList(statPacketBounsListVo);
        statPacketBounsDetailParentVo.setTodayBouns(statPacketActivity.getTodayChargeBonus()==null?0d:statPacketActivity.getTodayChargeBonus());
        statPacketBounsDetailParentVo.setTotalBouns(statPacketActivity.getChargeBonus()==null?0d:statPacketActivity.getChargeBonus());
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(statPacketBounsDetailParentVo);
        return busiResult;
    }
}
