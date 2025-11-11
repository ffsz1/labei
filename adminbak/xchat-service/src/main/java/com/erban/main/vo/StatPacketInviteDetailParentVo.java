package com.erban.main.vo;

import java.util.List;

public class StatPacketInviteDetailParentVo {
    private StatPacketInviteDetailVo inviteDetail;

    private List<StatPacketInviteRegisterListVo> inviteList;

    public StatPacketInviteDetailVo getInviteDetail() {
        return inviteDetail;
    }

    public void setInviteDetail(StatPacketInviteDetailVo inviteDetail) {
        this.inviteDetail = inviteDetail;
    }

    public List<StatPacketInviteRegisterListVo> getInviteList() {
        return inviteList;
    }

    public void setInviteList(List<StatPacketInviteRegisterListVo> inviteList) {
        this.inviteList = inviteList;
    }
}
