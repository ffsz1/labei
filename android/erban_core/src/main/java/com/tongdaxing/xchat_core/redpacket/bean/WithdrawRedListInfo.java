package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/27.
 */

public class WithdrawRedListInfo implements Serializable {
     /* "packetNum":100,
              "prodStauts":1,
              "seqNo":1
*/
    private int packetNum;
    private int prodStauts;
    private int seqNo;
    private boolean isWd = false;//是否可以提现

    public boolean isSelected;

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    private int packetId;

    public int getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(int packetNum) {
        this.packetNum = packetNum;
    }

    public int getProdStauts() {
        return prodStauts;
    }

    public void setProdStauts(int prodStauts) {
        this.prodStauts = prodStauts;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean isWd() {
        return isWd;
    }

    public void setWd(boolean wd) {
        isWd = wd;
    }
}
