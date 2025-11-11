package com.erban.web.controller.charge;

import com.erban.main.model.RedeemCode;
import com.erban.main.service.charge.RedeemCodeService;
import com.erban.web.common.BaseController;
import com.google.common.collect.Lists;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.http.HttpUitls;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 *
 * 兑换码
 *
 */
@Controller
@RequestMapping("/redeemcode")
public class RedeemCodeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RedeemCodeController.class);

    @Autowired
    private RedeemCodeService redeemCodeService;
    @Autowired
    private JedisService jedisService;

    /**
     *  使用兑换码
     *
     * @param code 兑换码
     * @param uid  兑换用户UID
     * @param deviceInfo 客户端设备信息
     * @return
     */
    @RequestMapping(value = "/use")
    @ResponseBody
    @Authorization
    public BusiResult useRedeemCode(HttpServletRequest request, String code, Long uid, DeviceInfo deviceInfo) {
        if (BlankUtil.isBlank(code) || code.length() > 20 || uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        // 限制同一IP的访问次数
        String clientIp = HttpUitls.getRealIpAddress(request);
        String ipCountKey = RedisKey.redeem_code_ip.getKey(DateTimeUtil.getTodayStr() + "_" + clientIp);
        Long ipCount = jedisService.incr(ipCountKey);
        if (ipCount != null && ipCount > 1000) {
            return new BusiResult(BusiStatus.NOAUTHORITY, "系统检测到您兑换操作太频繁，将停止您的兑换码充值功能", null);
        }

        // 判断用户是否存在
        if (!redeemCodeService.isExistUser(uid)) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }

        // 限制输入的错误次数
        String inputKey = RedisKey.redeem_code_input.getKey(DateTimeUtil.getTodayStr() + "_" + uid.toString());
        String inputCount = jedisService.read(inputKey);
        int errorCount = 0; // 输入的错误次数
        if (!BlankUtil.isBlank(inputCount)) {
            errorCount = Integer.valueOf(inputCount);
            if (errorCount >= 5) {
                return new BusiResult(BusiStatus.NOAUTHORITY, "对不起，您今天已累计5次输入有误，系统将停止您的兑换码充值功能", null);
            }
        }
        return redeemCodeService.useRedeemCode(code,uid,clientIp,deviceInfo,inputKey);
    }


    /**
     * 生成兑换码
     *
     * @param num 指定生成的数量
     * @return
     */
    @RequestMapping(value = "/gener")
    @ResponseBody
    public BusiResult generRedeemCode(Integer num, long amount, Integer len) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (num == null || num < 1) {
            num = 10;
        }
        if (len == null || len < 8) {
            len = 16;
        }

        // 用于存储生成的兑换码
        List<String> codes = Lists.newArrayList();
        int tmp = 0;
        while (tmp < num) {
            RedeemCode redeemCode = new RedeemCode();
            redeemCode.setCode(redeemCodeService.buildRandomCode(len));
            redeemCode.setAmount(amount);
            redeemCode.setUseStatus(1);
            redeemCode.setCreateTime(new Date());
            int result = redeemCodeService.insertRedeemCode(redeemCode);
            if (result == 1) {
                tmp++;
                codes.add(redeemCode.getCode());
            }
        }
//        busiResult.setData(codes);
        return busiResult;
    }

}
