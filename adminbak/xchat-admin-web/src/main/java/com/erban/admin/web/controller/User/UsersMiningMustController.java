package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.AdminConstants;
import com.erban.admin.main.service.dingtalk.DingtalkChatbotService;
import com.erban.admin.main.service.dingtalk.bo.DingtalkTextMessageBO;
import com.erban.admin.main.service.gift.GiftAdminService;
import com.erban.admin.main.service.record.UserService;
import com.erban.admin.main.service.user.UsersMiningMustService;
import com.erban.admin.main.vo.UsersMiningMustVO;
import com.erban.admin.main.vo.UsersVo;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Gift;
import com.github.pagehelper.PageInfo;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/miningmust")
public class UsersMiningMustController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UsersMiningMustController.class);

    @Autowired
    private DingtalkChatbotService chatbotService;

    @Autowired
    private UsersMiningMustService usersMiningMustService;

    @Autowired
    private UserService userService;

    @Autowired
    private GiftAdminService giftAdminService;

    /**
     * 获取中全服的历史记录
     *
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public void getList(Integer pageNumber, Integer pageSize, Long erbanNo, String startTime, String endTime) {
        PageInfo pageInfo = usersMiningMustService.getList(pageNumber, pageSize, erbanNo, startTime, endTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 增加全服中
     *
     * @param erbanNo 拉贝号
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public BusiResult add(HttpSession session, Long erbanNo, Integer giftId, Long inputGold, Long outputGold, Double rate) {
        BusiResult result;
        try {
            result = usersMiningMustService.addUsersMiningMust(erbanNo, giftId, getAdminId(), inputGold, outputGold, rate);
        } catch (Exception e) {
            logger.error("addManualRecomm error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }

        try {
            if (getAdminId() < 1000 || 10000 < getAdminId()) {
                String adminName = (String) session.getAttribute(AdminConstants.ADMIN_NAME);
                StringBuilder builder = new StringBuilder();
                builder.append("[ 管理后台").append(GlobalConfig.envName).append("预警 ]");
                builder.append(adminName);
                builder.append("为【拉贝号：");
                builder.append(erbanNo);
                builder.append("】配置必中礼物:>");
                String giftName = usersMiningMustService.queryGiftName(giftId);
                if (!giftName.isEmpty()) {
                    builder.append(giftName);
                } else {
                    builder.append(giftId);
                }
                chatbotService.send(new DingtalkTextMessageBO(builder.toString(), null, false));
            }
        } catch (Exception e) {
            logger.error("发送预警消息异常：", e);
        }
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param ernos 拉贝号，多个号用换行符分隔
     * @return
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public BusiResult<List<UsersMiningMustVO>> getUserInfoByErNos(String ernos, Integer giftId) {
        List<UsersVo> list = userService.getUserList(ernos);
        if (list.size() == 0) {
            return new BusiResult(BusiStatus.USERNOTEXISTS, null);
        }

        Gift gift = giftAdminService.get(giftId);
        if (gift == null) {
            return new BusiResult(BusiStatus.GIFT_NOT_EXISTS, null);
        }

        List<UsersMiningMustVO> usersMiningMustVOS = new ArrayList<>();
        list.stream().forEach(item -> {
            UsersMiningMustVO usersMiningMustVO = new UsersMiningMustVO();
            usersMiningMustVO.setAvatar(item.getAvatar());
            usersMiningMustVO.setErbanNo(item.getErbanNo());
            usersMiningMustVO.setGiftId(gift.getGiftId());
            usersMiningMustVO.setGiftName(gift.getGiftName());
            usersMiningMustVO.setNick(item.getNick());
            usersMiningMustVO.setGoldNum(item.getGoldNum());
            usersMiningMustVOS.add(usersMiningMustVO);
        });
        return new BusiResult(BusiStatus.SUCCESS, usersMiningMustVOS);
    }
}
