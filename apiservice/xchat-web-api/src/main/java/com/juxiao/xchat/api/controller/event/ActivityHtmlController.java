package com.juxiao.xchat.api.controller.event;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.domain.RankActDO;
import com.juxiao.xchat.manager.common.event.ActivityHtmlManager;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity/html")
@Api(tags = "排行榜接口")
public class ActivityHtmlController {
    @Resource
    private ActivityHtmlManager activityHtmlManager;

    /**
     * 查询收到礼物排名
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "rank", method = RequestMethod.GET)
    public WebServiceMessage queryRank(@RequestParam(value = "type", required = false) Integer type) throws WebServiceException {
        List<RankActDO> list = activityHtmlManager.queryList(type);
        return WebServiceMessage.success(list == null ? Lists.newArrayList() : list);
    }

    /**
     * 分类日排行
     *
     * @param type 房间类型
     * @return
     */
    @RequestMapping("roomRank")
    @ResponseBody
    public WebServiceMessage roomRank(Integer type) {
        return WebServiceMessage.success(activityHtmlManager.getRoomDayRank(type));
    }

    @RequestMapping("getActivityInfo")
    @ResponseBody
    public WebServiceMessage getActivityInfo(@RequestParam("activityId") String activityId) {
        return WebServiceMessage.success(activityHtmlManager.getActivityInfo(activityId));
    }
}
