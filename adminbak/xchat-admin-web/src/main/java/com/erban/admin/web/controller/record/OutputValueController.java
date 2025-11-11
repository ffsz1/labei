package com.erban.admin.web.controller.record;

import com.erban.admin.main.service.record.OutputValueService;
import com.erban.admin.main.vo.OutputValueParam;
import com.erban.admin.web.base.BaseController;
import com.xchat.common.result.BusiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/outputValue")
public class OutputValueController extends BaseController {
    @Autowired
    private OutputValueService outputValueService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getList(@RequestBody OutputValueParam outputValueParam){
        return outputValueService.getList(outputValueParam);
    }


}
