package com.juxiao.xchat.service.api.wish.impl;

import com.juxiao.xchat.dao.wish.LabelDao;
import com.juxiao.xchat.dao.wish.domain.Label;
import com.juxiao.xchat.service.api.wish.LabelService;
import com.juxiao.xchat.service.api.wish.vo.LabelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelDao labelDao;
    @Override
    public List<Label> getMeetLabels() {
        List<Label> labels = labelDao.listLabel("1");
        return labels;
    }

    @Override
    public List<Label> getPensonlLabels() {
        List<Label> labels = labelDao.listLabel("2");
        return labels;
    }

    @Override
    public LabelVo getLabels() {
        List<Label> mlabels = labelDao.listLabel("2");
        List<Label> plabels = labelDao.listLabel("1");
        LabelVo labelVo=new LabelVo();
        labelVo.setMeetLabels(mlabels);
        labelVo.setPersonalLabels(plabels);
        return labelVo;
    }
}
