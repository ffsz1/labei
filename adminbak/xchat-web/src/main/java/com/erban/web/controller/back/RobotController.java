package com.erban.web.controller.back;

import com.erban.main.service.RobotService;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/robot")
public class RobotController extends BaseController {
    @Autowired
    private RobotService robotService;

    @RequestMapping(value = "add",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult add(Long erbanNo){
        if(erbanNo==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return robotService.addRobotByErbanNo(erbanNo);
        } catch (Exception e) {
            logger.error("addRobot error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "deloneroom",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult deleteByErbanNo(Long erbanNo){
        if(erbanNo==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return robotService.removeRobotByErbanNo(erbanNo);
        } catch (Exception e) {
            logger.error("addRobot error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
