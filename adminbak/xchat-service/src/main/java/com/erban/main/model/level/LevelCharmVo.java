package com.erban.main.model.level;

/**
 * 用户经验值VO
 */
public class LevelCharmVo {
    private Long  uid;
    private String levelName;//等级
    private Double levelPercent;//金币等级百分比
    private Long leftGoldNum;//升到下一级所需的金币数量

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Double getLevelPercent() {
        return levelPercent;
    }

    public void setLevelPercent(Double levelPercent) {
        this.levelPercent = levelPercent;
    }

    public Long getLeftGoldNum() {
        return leftGoldNum;
    }

    public void setLeftGoldNum(Long leftGoldNum) {
        this.leftGoldNum = leftGoldNum;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
