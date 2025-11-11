package com.juxiao.xchat.api.controller.event;

import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.event.RankService;
import com.juxiao.xchat.service.api.event.vo.RankParentVo;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/allrank")
@Api(tags = "排行榜接口")
public class RankController {

    @Autowired
    private RankService rankService;

    @Autowired
    private AppVersionManager appVersionService;

    @Autowired
    private ChannelService channelService;

    /**
     * 查询各类礼物排行榜信息，H5页专用
     *
     * @param type     排行榜类型 1、魅力榜  2、土豪榜  3、房间榜
     * @param datetype 榜单统计周期类型：1、日榜；2周榜；3总榜
     * @param pageSize 页面数据条数
     * @return
     */
    @RequestMapping(value = "/geth5", method = RequestMethod.GET)
    public WebServiceMessage getH5RankParentVoList(HttpServletRequest request,
                                                   @RequestParam(value = "uid", required = false) Long uid,
                                                   @RequestParam(value = "type", required = false) Integer type,
                                                   @RequestParam(value = "datetype", required = false) Integer datetype,
                                                   @RequestParam(value = "channel", required = false) String channel,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "appid", required = false) String appid,
                                                   @RequestParam(value = "os", required = false) String os,
                                                   @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        if("1001".equals(appid) || "android".equals(os)){
            ChannelEnum channelEnum = ChannelEnum.nameOf(channel);
            ChannelAuditVO auditVo = channelService.checkAudit(channelEnum, appVersion, uid);
            if (auditVo != null && auditVo.isAudit()) {
                RankParentVo rankVo = rankService.getAuditingRankList(channelEnum, type, datetype);
                return WebServiceMessage.success(rankVo);
            }
            String ip = HttpServletUtils.getRealIp(request);
            if (appVersionService.checkAuditingVersion(os, appid, appVersion, ip, uid)) {
                RankParentVo rankVo = rankService.getAuditingRankList(channelEnum, type, datetype);
                return WebServiceMessage.success(rankVo);
            }
        }
        return WebServiceMessage.success(rankService.getH5RankList(type, datetype, pageSize));
    }

    /**
     * 查询当前用户排行榜信息，H5页专用
     *
     * @param type     排行榜类型：1巨星榜；2贵族榜；3房间榜
     * @param datetype 榜单统计周期类型：1、日榜；2周榜；3总榜
     * @param uid 页面数据条数
     * @return
     */
    @RequestMapping(value = "/getMeH5Rank", method = RequestMethod.GET)
    public WebServiceMessage getMeH5Rank(HttpServletRequest request,
                                         @RequestParam(value = "uid", required = false) Long uid,
                                         @RequestParam(value = "type", required = false) Integer type,
                                         @RequestParam(value = "datetype", required = false) Integer datetype,
                                         @RequestParam(value = "channel", required = false) String channel,
                                         @RequestParam(value = "appid", required = false, defaultValue = "1001") String appid,
                                         @RequestParam(value = "os", required = false) String os,
                                         @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException{
        return WebServiceMessage.success(rankService.getMeh5Rank(type, datetype, uid));
    }
}
