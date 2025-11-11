package com.juxiao.xchat.api.controller.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.guild.GuildHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 20:12
 * @作者： carl
 */
@RestController
@RequestMapping("/guild/hall")
public class GuildHallController {

    @Autowired
    private GuildHallService guildHallService;

    @RequestMapping("get")
    public WebServiceMessage getHall(Long hallId, Long uid) throws WebServiceException {
        return WebServiceMessage.success(guildHallService.getHallDetail(hallId, uid));
    }

}
