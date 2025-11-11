package com.erban.admin.main.bo;


/**
 * @author chris
 * @Title:
 * @date 2019-05-30
 * @time 11:08
 */
public class MoraAwardBO {

    private Integer[] giftId;

    private Integer[] num;

    private Integer probability;

    private Integer grade;

    private Integer id;

    private String json;


    public Integer[] getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer[] giftId) {
        this.giftId = giftId;
    }

    public Integer[] getNum() {
        return num;
    }

    public void setNum(Integer[] num) {
        this.num = num;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }


    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }


}
