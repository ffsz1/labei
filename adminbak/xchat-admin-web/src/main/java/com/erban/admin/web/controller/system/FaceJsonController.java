package com.erban.admin.web.controller.system;


import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.FacesDTO;
import com.erban.admin.main.service.system.FaceJsonAdminService;
import com.erban.admin.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/admin/face/json")
@ResponseBody
public class FaceJsonController extends BaseController{
    @Autowired
    private FaceJsonAdminService faceJsonService;


    @RequestMapping("/getList")
    @ResponseBody
    public void getList()throws Exception{
        List<FacesDTO> list = faceJsonService.getFaceJson();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",1);
        jsonObject.put("rows",list);
        writeJson(jsonObject.toJSONString());
    }


}
