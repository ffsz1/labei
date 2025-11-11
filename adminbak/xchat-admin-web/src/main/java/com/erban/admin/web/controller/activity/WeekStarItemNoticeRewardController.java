package com.erban.admin.web.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.activity.WeekStarItemNoticeRewardService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.WeekStarItemRewardDTO;
import com.erban.main.model.GiftCar;
import com.erban.main.model.Headwear;
import com.erban.main.model.WeekStarItemNoticeReward;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.headwear.HeadwearService;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chris
 * @Title: 周星预告奖励
 * @date 2019-05-17
 * @time 17:56
 */
@Controller
@RequestMapping("/admin/week/star/item/notice/reward")
public class WeekStarItemNoticeRewardController extends BaseController {

    @Autowired
    private WeekStarItemNoticeRewardService weekStarItemNoticeRewardService;

    @Autowired
    private HeadwearService headwearService;

    @Autowired
    private GiftCarService giftCarService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<WeekStarItemRewardDTO> pageInfo  = weekStarItemNoticeRewardService.getList(searchText,getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getGifts")
    @ResponseBody
    public BusiResult getGifts(){
        return new BusiResult(BusiStatus.SUCCESS,weekStarItemNoticeRewardService.getGifts());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(WeekStarItemNoticeReward weekStarItemReward, boolean isEdit )
    {
        if(weekStarItemReward == null) {
            writeJson(false, "参数有误");
            return;
        } else {
            try {

               if(weekStarItemReward.getType() == 1){
                   GiftCar giftCar = giftCarService.getOneByJedisId(weekStarItemReward.getItemId().toString());
                   if(giftCar == null){
                       writeJson(false, "座驾奖项不存在,请确认!");
                       return;
                   }
               }else{
                   Headwear headwear = headwearService.getOneByJedisId(weekStarItemReward.getItemId().toString());
                   if(headwear == null){
                       writeJson(false, "头饰奖项不存在,请确认!");
                       return;
                   }
               }


                int result = weekStarItemNoticeRewardService.save(weekStarItemReward, isEdit,getAdminId());
                if(result > 0) {
                    writeJson(true, "保存成功");
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
                logger.error("Failed to save saveWeekStarItemNoticeReward. Cause by {}", e.getCause().getMessage());
            }
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public void del(Long id) {
        try {
            int result = weekStarItemNoticeRewardService.deleteById(id);
            if(result > 0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete WeekStarItemNoticeReward, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id")Long id)
    {
        JSONObject jsonObject = new JSONObject();
        WeekStarItemNoticeReward weekStarItemNoticeReward = weekStarItemNoticeRewardService.getById(id);
        if(weekStarItemNoticeReward != null) {
            jsonObject.put("entity", weekStarItemNoticeReward);
        }
        writeJson(jsonObject.toJSONString());
    }

}
