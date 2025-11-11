package com.erban.admin.web.controller.system;

import com.erban.admin.web.base.BaseController;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 解除登录限制controller
 */
@Controller
@RequestMapping("/admin/deblocking")
public class DeblockingController extends BaseController {

    @Autowired
    protected JedisService jedisService;

    /**
     * 解封登录限制
     *
     * @param erbanNo
     */
    @RequestMapping(value = "/deblocking")
    @ResponseBody
    public BusiResult deblocking(Long erbanNo) {
        if (erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        String key = jedisService.get(RedisKey.admin_login_error_count.getKey(erbanNo.toString()));
        if (key == null) {
            return new BusiResult<>(BusiStatus.ISUNBANGSTATUS, "该用户未被限制登录", null);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            jedisService.del(RedisKey.admin_login_error_count.getKey(erbanNo.toString()));
        } catch (Exception e) {
            logger.error("deblocking error", e);
            busiResult.setCode(BusiStatus.SERVERERROR.value());
        }
        return busiResult;
    }
}
