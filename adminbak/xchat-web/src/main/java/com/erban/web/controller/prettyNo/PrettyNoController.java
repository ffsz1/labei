package com.erban.web.controller.prettyNo;


import com.erban.main.service.prettyNo.PrettyNoService;
import com.erban.web.common.BaseController;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/1/16.
 *
 */
@Controller
@RequestMapping("/prettyNo/")
public class PrettyNoController extends BaseController{
    @Autowired
    private PrettyNoService prettyNoService;

    //申请接口
    @RequestMapping(value ="checkPrettyNo",method = RequestMethod.GET)
    @ResponseBody
    public BusiResult applyPrettyNo(Long prettyNo, Long uid){
        if(prettyNo==0 ||uid ==0){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return prettyNoService.applyPrettyNo(prettyNo,uid);
    }


    /**
     * 查看申请状态
     * @Params uid
     * @return
     */
    @RequestMapping(value = "checkStatus")
    @ResponseBody
    public BusiResult checkStatus(Long uid){
        return prettyNoService.queryCheckResult(uid);
    }


}
