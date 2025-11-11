package com.juxiao.xchat.service.api.wish.vo;

import com.juxiao.xchat.dao.wish.domain.Label;
import java.util.List;

public class LabelVo {
    private List<Label> meetLabels;
    private List<Label> personalLabels;

    public List<Label> getMeetLabels() {
        return meetLabels;
    }

    public void setMeetLabels(List<Label> meetLabels) {
        this.meetLabels = meetLabels;
    }

    public List<Label> getPersonalLabels() {
        return personalLabels;
    }

    public void setPersonalLabels(List<Label> personalLabels) {
        this.personalLabels = personalLabels;
    }
}
