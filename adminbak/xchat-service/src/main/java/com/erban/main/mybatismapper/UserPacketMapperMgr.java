package com.erban.main.mybatismapper;

import com.erban.main.model.UserPacket;
import com.erban.main.model.UserPacketRecord;
import com.erban.main.model.beanmap.StatPacketInviteRegisterMap;
import com.erban.main.vo.StatPacketBounsListVo;
import com.erban.main.vo.StatPacketInviteRegisterListVo;

import java.util.Date;
import java.util.List;

public interface UserPacketMapperMgr {

    public void addUserPacketNumTransactional(UserPacket userPacket);

    public int getStatPacketRankSeqNoByPacketNum(double packetNum);

    public List<UserPacket> queryUserPacketListBy();

    public List<StatPacketInviteRegisterListVo> queryStatPacketInviteRegisterMapList(Long uid);

    public List<StatPacketBounsListVo> queryInviteBonusList(Long uid);

}
