package com.erban.main.service.room;

import com.erban.main.model.RoomReward;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.mybatismapper.RoomRewardMapper;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.user.FansService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.RoomRewardVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by liuguofu on 2017/5/25.
 */
@Service
public class RoomRewardService {
    private static final Logger logger = LoggerFactory.getLogger(RoomRewardService.class);
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private FansService fansService;
    @Autowired
    private RoomMapper roomMapper;
    private static int defaultServDura=30;

    @Autowired
    private RoomRewardMapper roomRewardMapper;

    @Transactional
    public BusiResult<RoomRewardVo> saveRoomReward(Long uid,Long rewardMoney,int servDura){
        BusiResult<RoomRewardVo> busiResult=new BusiResult<RoomRewardVo>(BusiStatus.SUCCESS);
        servDura=defaultServDura;
        String rewardId= UUIDUitl.get();
        Date date=new Date();
        RoomReward roomReward=new RoomReward();
        roomReward.setRewardId(rewardId);
        roomReward.setUid(uid);
        roomReward.setRewardMoney(rewardMoney);
        roomReward.setServDura(servDura);
        roomReward.setCreateTime(date);
        roomReward.setFinishTime(date);
        roomReward.setRewardStatus(new Byte("1"));
        RoomRewardVo roomRewardVo=convertRoomRewardVo(roomReward);
        busiResult.setData(roomRewardVo);
        return busiResult;
    }

    private void insertRoomReward(RoomReward roomReward){
        roomRewardMapper.insert(roomReward);
    }
    private RoomRewardVo convertRoomRewardVo(RoomReward roomReward){
        RoomRewardVo roomRewardVo=new RoomRewardVo();
        roomRewardVo.setRewardId(roomReward.getRewardId());
        roomRewardVo.setServDura(roomReward.getServDura());
        roomRewardVo.setRewardMoney(roomReward.getRewardMoney());
        roomRewardVo.setUid(roomReward.getUid());
        return roomRewardVo;
    }

}
