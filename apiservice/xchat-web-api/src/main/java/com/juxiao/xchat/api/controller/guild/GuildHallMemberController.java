package com.juxiao.xchat.api.controller.guild;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import com.juxiao.xchat.service.api.guild.GuildHallMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 16:32
 * @作者： carl
 */
@RestController
@RequestMapping("/guild/hall/member")
public class GuildHallMemberController {

    @Autowired
    private GuildHallMemberService guildHallMemberService;

    @Autowired
    private GuildHallMemberManager guildHallMemberManager;

    /**
     * 我的公会 → 用户的相关数据
     * @param uid
     * @return
     * @throws Exception
     */
    @SignVerification
    @RequestMapping("getGuildMemberCommonInfo")
    public WebServiceMessage getGuildMemberInfo(Long uid) throws Exception {
        return WebServiceMessage.success(guildHallMemberService.getGuildMemberCommonInfo(uid));
    }

    @RequestMapping("getGuildMembers")
    public WebServiceMessage getGuildMembers(Long guildId) throws Exception {
        return WebServiceMessage.success(guildHallMemberManager.getGuildMembers(guildId));
    }

    @RequestMapping("getHallMembers")
    public WebServiceMessage getHallMembers(Long hallId) throws Exception {
        return WebServiceMessage.success(guildHallMemberManager.getHallMembers(hallId));
    }

    /**
     * 成员页所需数据
     */
    @SignVerification
    @RequestMapping("getGuildMemberPageInfo")
    public WebServiceMessage getGuildMemberPageInfo(Long memberUid) throws Exception {
        return WebServiceMessage.success(guildHallMemberService.getGuildMemberPageInfo(memberUid));
    }
}
