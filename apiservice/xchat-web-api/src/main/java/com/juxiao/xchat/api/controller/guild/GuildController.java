package com.juxiao.xchat.api.controller.guild;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.guild.GuildService;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 11:33
 * @作者： carl
 */
@RestController
@RequestMapping("/guild")
public class GuildController {

    @Autowired
    private GuildService guildService;

    /**
     * 公会推荐列表
     * @param param
     * @return
     */
    @RequestMapping("/getRecommendList")
    public WebServiceMessage getRecommendList(IndexParam param) {
        return WebServiceMessage.success(guildService.getRecommendList(param));
    }

    /**
     * 公会信息，包括厅信息
     * @param guildId
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping("/get")
    public WebServiceMessage getDetail(Long guildId, Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildService.getDetail(guildId, uid));
    }

    /**
     * 搜索公会
     * @param key
     * @return
     */
    @SignVerification
    @RequestMapping("/search")
    public WebServiceMessage search(String key, Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildService.search(key, uid));
    }
}
