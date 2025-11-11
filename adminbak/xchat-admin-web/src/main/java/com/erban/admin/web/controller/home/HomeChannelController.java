package com.erban.admin.web.controller.home;


import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.channel.HomeChannelExpandService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.HomeChannel;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laizhilong
 * @Title: 渠道管理
 * @Package com.erban.admin.web.controller.User
 * @date 2018/8/16
 * @time 09:54
 */
@Controller
@RequestMapping("/admin/home/channel")
public class HomeChannelController extends BaseController {
    @Autowired
    private HomeChannelExpandService homeChannelExpandService;

    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(String channel, Integer groupId) throws Exception{
        JSONObject jsonObject = new JSONObject();
        PageInfo<HomeChannel> pageInfo = homeChannelExpandService.getListWithPage(channel, groupId, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("/save")
    @ResponseBody
    public void save(HomeChannel homeChannel, boolean isEdit) {
        int result = -1;
        if (homeChannel != null) {
            try {
                result = homeChannelExpandService.save(homeChannel, isEdit);
            } catch (Exception e) {
                logger.warn("save fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public void getOne(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {

            HomeChannel channel = homeChannelExpandService.getOne(id);
            if (channel != null) {
                jsonObject.put("entity", channel);
            }
        }
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("/del")
    @ResponseBody
    public BusiResult del(Long id) {
        BusiResult busiResult = new BusiResult();
        try {
            int result = homeChannelExpandService.delete(id);
            if(result > 0){
                busiResult.setCode(200);
                busiResult.setMessage("保存成功");
            }
        } catch (Exception e) {
            logger.warn("save fail,cause by " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage(BusiStatus.BUSIERROR.getReasonPhrase());
        }
        return busiResult;
    }

    @RequestMapping("/list")
    @ResponseBody
    public BusiResult channelList(){
        return new BusiResult(BusiStatus.SUCCESS, homeChannelExpandService.channelList());
    }

    @RequestMapping("/groupList")
    @ResponseBody
    public BusiResult groupList(){
        return new BusiResult(BusiStatus.SUCCESS, homeChannelExpandService.groupList());
    }

    @RequestMapping("/addGroup")
    @ResponseBody
    public BusiResult addGroup(String name){
        BusiResult busiResult = new BusiResult();
        try {
            int result = homeChannelExpandService.saveGroup(name);
            if(result > 0){
                busiResult.setCode(200);
                busiResult.setMessage("保存成功");
            }
        } catch (Exception e) {
            logger.warn("save fail,cause by " + e.getMessage(), e);
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage(e.getMessage());
        }
        return busiResult;
    }

}
