package com.erban.admin.web.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.AccompayRecommService;
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
@RequestMapping("/admin/accompay")
public class AccompayRecommController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccompayRecommController.class);

    @Autowired
    private AccompayRecommService accompayRecommService;

    /**
     * 获取陪玩推荐列表
     *
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public void getHomeHotRecommVoList(Integer pageNumber, Integer pageSize, Long erbanNo){
        PageInfo pageInfo = accompayRecommService.getAccompayRecommByList(pageNumber, pageSize, erbanNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 增加陪玩推荐
     *
     * @param erbanNo 拉贝号
     * @param seqNo   排序
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public BusiResult addAccompayManualRecomm(Long erbanNo, int seqNo, String startTimeString, String endTimeString) {
        try {
            if(startTimeString==null||endTimeString==null){
                return new BusiResult(BusiStatus.PARAMERROR);
            }
            return accompayRecommService.addAccompayManualRecomm(erbanNo, seqNo, startTimeString, endTimeString);
        } catch (Exception e) {
            logger.error("addManualRecomm error,erbanNo=" + erbanNo + "&seqNo=" + seqNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除热门推荐
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    public BusiResult deleteAccompayManualRecomm(Integer id) {
        try {
            return accompayRecommService.deleteAccompayManualRecomm(id);
        } catch (Exception e) {
            logger.error("deleteAccompayManualRecomm error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 获取单个热门推荐
     *
     * @param id
     */
    @RequestMapping(value = "/getOneRecomm")
    @ResponseBody
    public BusiResult getOneAccompayManualRecomm(Integer id){
        try {
            return accompayRecommService.getOneAccompayManualRecomm(id);
        } catch (Exception e){
            logger.error("getOneAccompayManualRecomm error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 更新推荐
     *
     *
     */
    @RequestMapping(value = "/updateRecomm")
    @ResponseBody
    public BusiResult updateRecomm(Integer id, Integer seqNo, String startTimeString, String endTimeString){
        try {
            return accompayRecommService.updateRecomm(id,seqNo,startTimeString,endTimeString);
        } catch (Exception e){
            logger.error("updateRecomm error:",e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }


    /**
     * 删除热门推荐
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setting")
    @ResponseBody
    public BusiResult settingAccompayManualRecomm(Integer id) {
        try {
            return accompayRecommService.settingAccompayManualRecomm(id);
        } catch (Exception e) {
            logger.error("settingAccompayManualRecomm error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }


}


