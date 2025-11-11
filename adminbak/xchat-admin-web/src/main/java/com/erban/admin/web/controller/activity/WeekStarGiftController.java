package com.erban.admin.web.controller.activity;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.activity.WeekStarGiftService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.model.Gift;
import com.erban.main.model.WeekStarGift;
import com.erban.main.service.gift.GiftService;
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
 * @Title:
 * @date 2019-05-17
 * @time 16:35
 */
@Controller
@RequestMapping("/admin/week/star/gift")
public class WeekStarGiftController extends BaseController {
    @Autowired
    private WeekStarGiftService weekStarGiftService;

    @Autowired
    private GiftService giftService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public void getList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<WeekStarGiftDTO> pageInfo = weekStarGiftService.getList(searchText, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(WeekStarGift weekStarGift, boolean isEdit) {
        if (weekStarGift == null) {
            writeJson(false, "参数有误");
            return;
        } else {
            try {
                Gift gift = giftService.getValidGiftById(weekStarGift.getGiftId());
                if (gift == null) {
                    writeJson(false, "礼物不存在,请确认");
                    return;
                }

                WeekStarGift item = weekStarGiftService.getByGiftId(weekStarGift.getGiftId());
                if (item != null) {
                    writeJson(false, "该周星礼物已存在故不能重复添加!");
                    return;
                }

                if (!isEdit) {
                    int count = weekStarGiftService.getEffectiveCount();
                    if (count >= 3) {
                        writeJson(false, "本周周星礼物不能超过三个!");
                        return;
                    }
                }
                int result = weekStarGiftService.save(weekStarGift, isEdit, getAdminId());
                if (result > 0) {
                    writeJson(true, "保存成功");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to save saveWeekStarGift. Cause by {}", e.getCause().getMessage());
            }
        }
        writeJson(false, "保存失败");
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public void del(Long id) {
        try {
            int result = weekStarGiftService.deleteById(id);
            if (result > 0) {
                writeJson(true, "删除成功");
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to delete weekStarGift, Cause by {}", e.getCause().getMessage());
        }
        writeJson(false, "删除失败");
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public void get(@RequestParam("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        WeekStarGift weekStarGift = weekStarGiftService.getById(id);
        if (weekStarGift != null) {
            jsonObject.put("entity", weekStarGift);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getNormalGifts")
    @ResponseBody
    public BusiResult getNormalGifts() {
        return new BusiResult(BusiStatus.SUCCESS, weekStarGiftService.getNormalGifts());
    }
}
