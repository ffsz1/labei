package com.erban.main.model;

import java.util.Date;

public class McoinPkInfo {
    private Long id;

    private Integer term;

    private String title;

    private String redAnswer;

    private String blueAnswer;

    private String redPic;

    private String bluePic;

    private Integer redPolls;

    private Integer bluePolls;

    private String lotteryTime;

    private Integer carveUpMcoinNum;

    private Byte pkStatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getRedAnswer() {
        return redAnswer;
    }

    public void setRedAnswer(String redAnswer) {
        this.redAnswer = redAnswer == null ? null : redAnswer.trim();
    }

    public String getBlueAnswer() {
        return blueAnswer;
    }

    public void setBlueAnswer(String blueAnswer) {
        this.blueAnswer = blueAnswer == null ? null : blueAnswer.trim();
    }

    public String getRedPic() {
        return redPic;
    }

    public void setRedPic(String redPic) {
        this.redPic = redPic == null ? null : redPic.trim();
    }

    public String getBluePic() {
        return bluePic;
    }

    public void setBluePic(String bluePic) {
        this.bluePic = bluePic == null ? null : bluePic.trim();
    }

    public Integer getRedPolls() {
        return redPolls;
    }

    public void setRedPolls(Integer redPolls) {
        this.redPolls = redPolls;
    }

    public Integer getBluePolls() {
        return bluePolls;
    }

    public void setBluePolls(Integer bluePolls) {
        this.bluePolls = bluePolls;
    }

    public String getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(String lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public Integer getCarveUpMcoinNum() {
        return carveUpMcoinNum;
    }

    public void setCarveUpMcoinNum(Integer carveUpMcoinNum) {
        this.carveUpMcoinNum = carveUpMcoinNum;
    }

    public Byte getPkStatus() {
        return pkStatus;
    }

    public void setPkStatus(Byte pkStatus) {
        this.pkStatus = pkStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
