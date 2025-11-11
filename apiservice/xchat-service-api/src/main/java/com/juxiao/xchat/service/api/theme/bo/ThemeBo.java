package com.juxiao.xchat.service.api.theme.bo;

import com.juxiao.xchat.dao.theme.domain.Theme;

public class ThemeBo {
    private Long id;
    private String name;//名称
    private String state;//状态
    private String remarks;//备注
    private String pictureUrl; //图片url
    private Integer num; //发帖参与量

    public Theme getTheme(){
        Theme theme=new Theme();
        theme.setId(this.getId());
        theme.setName(this.getName());
        theme.setState(this.getState());
        theme.setRemarks(this.getRemarks());
        theme.setNum(this.num);
        theme.setPictureUrl(this.pictureUrl);
        return theme;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
