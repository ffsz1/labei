package com.juxiao.xchat.api.controller.wish;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.wish.domain.Label;
import com.juxiao.xchat.service.api.wish.LabelService;
import com.juxiao.xchat.service.api.wish.vo.LabelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;
    @RequestMapping("/get/mlabels")
    public WebServiceMessage getMeetLabel(){
        List<Label> meetLabels = labelService.getMeetLabels();
        return WebServiceMessage.success(meetLabels);
    }
    @RequestMapping("/get/plabels")
    public WebServiceMessage getPensonalLabel(){
        List<Label> pensonalLabels = labelService.getPensonlLabels();
        return WebServiceMessage.success(pensonalLabels);
    }
    @RequestMapping("/getlabels")
    public WebServiceMessage getLabels(){
        LabelVo labels = labelService.getLabels();
        return WebServiceMessage.success(labels);
    }
}
