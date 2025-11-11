package com.erban.main.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author author
 */
public class ExpressWall implements Serializable {

    private static final long serialVersionUID = 1530673527045L;


    /**
     * 主键
     * <p>
     * isNullAble:0
     */
    private Long id;

    /**
     * 发送礼物的用户
     * isNullAble:1
     */
    private Long sendUid;

    /**
     * 礼物接收人
     * isNullAble:1
     */
    private Long receiveUid;

    /**
     * 礼物
     * isNullAble:1
     */
    private Integer giftId;

    /**
     * 礼物数量
     * isNullAble:1
     */
    private Integer giftNum;

    /**
     * 礼物总金额
     * isNullAble:1
     */
    private Long totalGold;

    /**
     * 创建时间
     * isNullAble:1
     */
    private Date createTime;

    /**
     * 表白留言
     * isNullAble:1
     */
    private String message;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSendUid(Long sendUid) {
        this.sendUid = sendUid;
    }

    public Long getSendUid() {
        return this.sendUid;
    }

    public void setReceiveUid(Long receiveUid) {
        this.receiveUid = receiveUid;
    }

    public Long getReceiveUid() {
        return this.receiveUid;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getGiftId() {
        return this.giftId;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "ExpressWall{" +
                "id=" + id +
                ", sendUid=" + sendUid +
                ", receiveUid=" + receiveUid +
                ", giftId=" + giftId +
                ", totalGold=" + totalGold +
                ", createTime=" + createTime +
                ", message='" + message + '\'' +
                '}';
    }
}
