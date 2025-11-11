package com.erban.web.controller.room;

import com.erban.main.model.RoomTag;
import com.erban.main.service.AppVersionService;
import com.erban.main.service.room.RoomTagService;
import com.erban.web.common.BaseController;
import com.google.common.collect.Lists;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/room/tag")
public class RoomTagController extends BaseController {
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    AppVersionService appVersionService;

    /**
     * 顶部标签
     */
    @RequestMapping(value = "/top")
    @ResponseBody
    public BusiResult getTopTags(@RequestParam("os") String os, @RequestParam("appVersion")String appVersion) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
//            // IOS新版本在审核期内的首页数据要做特殊处理
//            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion)) {
//                busiResult.setData(Lists.newArrayList());
//                return busiResult;
//            }
            List<RoomTag> list = roomTagService.getTopTagList();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getTopTags error" ,e);
            busiResult.setCode(BusiStatus.SERVEXCEPTION.value());
            busiResult.setMessage("系统错误，请稍候再试");
        }
        return busiResult;
    }

    /**
     * 分类标签
     */
    @RequestMapping(value = "/classification")
    @ResponseBody
    public BusiResult getSearchTags() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<RoomTag> list = roomTagService.getSearchTags();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getSearchTags error" ,e);
            busiResult.setCode(BusiStatus.SERVEXCEPTION.value());
            busiResult.setMessage("系统错误，请稍候再试");
        }
        return busiResult;
    }

    /**
     * 所有有效标签，房间设置时需要获取所有标签
     */
    @RequestMapping(value = "/all")
    @ResponseBody
    public BusiResult getAllTags() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<RoomTag> list = roomTagService.getAllTagList();
            busiResult.setData(list);
        } catch (Exception e) {
            logger.error("getAllTags error" ,e);
            busiResult.setCode(BusiStatus.SERVEXCEPTION.value());
            busiResult.setMessage("系统错误，请稍候再试");
        }
        return busiResult;
    }



}
