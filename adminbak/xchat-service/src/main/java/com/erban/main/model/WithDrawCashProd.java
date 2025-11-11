package com.erban.main.model;

public class WithDrawCashProd {
    private String cashProdId;

    private String cashProdName;

    private Long diamondNum;

    private Long cashNum;

    private Integer seqNo;

    public String getCashProdId() {
        return cashProdId;
    }

    public void setCashProdId(String cashProdId) {
        this.cashProdId = cashProdId == null ? null : cashProdId.trim();
    }

    public String getCashProdName() {
        return cashProdName;
    }

    public void setCashProdName(String cashProdName) {
        this.cashProdName = cashProdName == null ? null : cashProdName.trim();
    }

    public Long getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Long diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Long getCashNum() {
        return cashNum;
    }

    public void setCashNum(Long cashNum) {
        this.cashNum = cashNum;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
