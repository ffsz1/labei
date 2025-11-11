package com.erban.main.vo;

public class WithDrawPacketCashProdVo implements Comparable<WithDrawPacketCashProdVo> {

    private Integer packetId;

    public void setPacketId(Integer packetId) {
        this.packetId = packetId;
    }

    public Integer getPacketId() {

        return packetId;
    }

    private Double packetNum;

    private Byte prodStauts;

    private Integer seqNo;

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public void setProdStauts(Byte prodStauts) {
        this.prodStauts = prodStauts;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Double getPacketNum() {

        return packetNum;
    }

    public Byte getProdStauts() {
        return prodStauts;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    @Override
    public int compareTo(WithDrawPacketCashProdVo withDrawPacketCashProdVo) {
        Integer seqNoVo = withDrawPacketCashProdVo.seqNo;
        Integer seqNoThis = this.seqNo;
        if (seqNoVo > seqNoThis) {
            return 1;
        } else if (seqNoVo < seqNoThis) {
            return -1;
        } else {
            return 0;
        }
    }
}
