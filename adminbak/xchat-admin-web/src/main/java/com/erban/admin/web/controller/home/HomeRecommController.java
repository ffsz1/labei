package com.erban.admin.web.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.room.HomeHotManualRecommService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/homerecomm")
public class HomeRecommController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(HomeRecommController.class);
    @Autowired
    private HomeHotManualRecommService homeHotManualRecommService;

    /**
     * 获取热门推荐列表
     *
     * @return
     */
    @RequestMapping(value = "/hot/voList")
    @ResponseBody
    public void getHomeHotRecommVoList(Integer pageNumber, Integer pageSize, Long erbanNo, Integer viewType) {
        PageInfo pageInfo = homeHotManualRecommService.getHomeHotRecommVoList(pageNumber, pageSize, erbanNo, viewType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 增加热门推荐
     *
     * @param erbanNo 拉贝号
     * @param seqNo   排序
     * @return
     */
    @RequestMapping(value = "/hot/add")
    @ResponseBody
    public BusiResult addManualRecomm(Long erbanNo, int seqNo, String startTimeString, String endTimeString,
                                      Integer viewType) {
        try {
            if (startTimeString == null || endTimeString == null) {
                return new BusiResult(BusiStatus.PARAMERROR);
            }
            return homeHotManualRecommService.addHomeHotManualRecomm(erbanNo, seqNo, startTimeString, endTimeString,
                    viewType);
        } catch (Exception e) {
            logger.error("addManualRecomm error,erbanNo=" + erbanNo + "&seqNo=" + seqNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除热门推荐
     *
     * @param recommId
     * @return
     */
    @RequestMapping(value = "/hot/del")
    @ResponseBody
    public BusiResult deleteManualRecomm(Integer recommId) {
        try {
            return homeHotManualRecommService.deleteHomManualRecomm(recommId);
        } catch (Exception e) {
            logger.error("deleteManualRecomm error,recommId=" + recommId, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 获取单个热门推荐
     *
     * @param recommId
     * @return
     */
    @RequestMapping(value = "/hot/getOneRecomm")
    @ResponseBody
    public BusiResult getOneRecomm(Integer recommId) {
        try {
            return homeHotManualRecommService.getOneRecomm(recommId);
        } catch (Exception e) {
            logger.error("getOneRecomm error,recommId=" + recommId, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 更新推荐
     *
     * @param recommId
     * @param seqNo
     * @param startTimeString
     * @param endTimeString
     * @param viewType
     * @return
     */
    @RequestMapping(value = "/hot/updateRecomm")
    @ResponseBody
    public BusiResult updateRecomm(Integer recommId, Integer seqNo, String startTimeString, String endTimeString,
                                   Integer viewType) {
        try {
            return homeHotManualRecommService.updateRecomm(recommId, seqNo, startTimeString, endTimeString, viewType);
        } catch (Exception e) {
            logger.error("updateRecomm error:", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 获取首页推荐规则
     *
     * @return
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    public BusiResult getRule() {
        try {
            return homeHotManualRecommService.getRule();
        } catch (Exception e) {
            logger.error("GetRule Error: ", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 更新首页推荐规则
     *
     * @param people 人数
     * @param gift   礼物流水
     * @param back   背包流水
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public BusiResult saveRule(Integer people, Integer gift, Integer back) {
        try {
            return homeHotManualRecommService.saveRule(people, gift, back);
        } catch (Exception e) {
            logger.error("SaveRule Error: ", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }
}


