package com.erban.web.controller.home;

import com.erban.main.model.FaceJson;
import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleZip;
import com.erban.main.model.SysConf;
import com.erban.main.service.SysConfService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleZipService;
import com.erban.main.service.room.FaceJsonService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.AppInitVo;
import com.erban.main.vo.SplashVo;
import com.erban.web.common.BaseController;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class ClientController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private FaceJsonService faceJsonService;
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private NobleRightService nobleRightService;
    @Autowired
    private NobleZipService nobleZipService;

    /**
     * APP启动拉取的初始化数据
     *
     * @return
     */
    @RequestMapping("/init")
    @ResponseBody
    public BusiResult startInit(String os, String appVersion, HttpServletRequest request) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            FaceJson faceJson = faceJsonService.getValidFaceJson(os, appVersion, request);  // 表情包的JSON数据
            SplashVo splashVo = sysConfService.getSplashConf();      // 闪屏数据
            List<NobleRight> rights = nobleRightService.getNobleRightList();  // 贵族特权列表
            NobleZip nobleZip = nobleZipService.getValidNobleZip();

            AppInitVo appInitVo = new AppInitVo();
            appInitVo.setFaceJson(faceJson);
            appInitVo.setSplashVo(splashVo);
            appInitVo.setNobleZip(coverNobleZip(nobleZip));
            appInitVo.setRights(rights);

            busiResult.setData(appInitVo);
        } catch (Exception e) {
            logger.error("startInit error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }

    @RequestMapping("/configure")
    @ResponseBody
    public BusiResult getConfigure(String idfa) {
        try {
            Map<String, Object> map = new HashMap<>();
            SysConf isExchangeAwards = sysConfService.getSysConfById("is_exchange_awards");
            SysConf timestamps = sysConfService.getSysConfById("timestamps");
            SysConf micInListOption = sysConfService.getSysConfById("mic_in_list_option");
            SysConf lotteryBoxOption = sysConfService.getSysConfById("lottery_box_option");
            SysConf greenRoomIndex = sysConfService.getSysConfById("green_room_index");
            if(isExchangeAwards!=null){
                map.put("isExchangeAwards", isExchangeAwards.getConfigValue());
            }else {
                map.put("isExchangeAwards", 0);
            }
            if(timestamps!=null){
                map.put("timestamps", timestamps.getConfigValue());
            }else {
                map.put("timestamps", 1);
            }
            if(micInListOption!=null){
                map.put("micInListOption", micInListOption.getConfigValue());
            }else {
                map.put("micInListOption", 0);
            }
            if(lotteryBoxOption!=null){
                map.put("lottery_box_option", lotteryBoxOption.getConfigValue());
            }else {
                map.put("lottery_box_option", 0);
            }
            if(greenRoomIndex!=null){
                map.put("greenRoomIndex", greenRoomIndex.getConfigValue());
            }else {
                map.put("greenRoomIndex", 6);
            }
            if(StringUtils.isNotBlank(idfa)){
                jedisService.hset(RedisKey.idfa.getKey(), idfa, "1");
            }
            return new BusiResult(BusiStatus.SUCCESS, map);
        } catch (Exception e) {
            logger.error("getConfigure error", e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    private NobleZip coverNobleZip(NobleZip nobleZip) {
        if (nobleZip == null) {
            return null;
        }
        nobleZip.setCreateTime(null);
        nobleZip.setId(null);
        return nobleZip;
    }
}
