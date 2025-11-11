package com.juxiao.xchat.api.controller.charge;

import com.alibaba.fastjson.JSON;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import com.juxiao.xchat.manager.external.weixin.WeixinAuthManager;
import com.juxiao.xchat.manager.external.weixin.conf.WeixinConf;
import com.juxiao.xchat.manager.external.weixin.ret.SnsapiBaseinfoRet;
import com.juxiao.xchat.service.api.charge.DiamondExchangeService;
import com.juxiao.xchat.service.api.charge.WithDrawService;
import com.juxiao.xchat.service.api.charge.vo.FinancialAccountVO;
import com.juxiao.xchat.service.api.charge.vo.UserWithdrawVO;
import com.juxiao.xchat.service.api.charge.vo.WithDrawCashVO;
import com.juxiao.xchat.service.api.user.AccountsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信公众号提现接口
 *
 * @author chris
 * @Title:
 * @date 2019-05-07 14:12
 */
@Slf4j
@RestController
@RequestMapping("/wxPublic")
@Api(tags = "微信公众号提现接口")
public class WxPublicWithDrawController {
    @Resource
    private WeixinConf weixinConf;

    @Resource
    private WeixinAuthManager authManager;

    @Resource
    private AccountsService accountsService;

    @Resource
    private WithDrawService withDrawService;

    @Resource
    private DiamondExchangeService exchangeService;

    @ApiOperation(value = "微信公众号获取openId")
    @ResponseBody
    @RequestMapping(value = "/snsapi/baseinfo/scan/get", method = RequestMethod.GET)
    public WebServiceMessage getOpenId(HttpServletResponse response, @RequestParam("code") String code,
                                       @RequestParam(value = "state", required = false) String state) {
        WXUserInfoVO userInfo = null;
        try {
            SnsapiBaseinfoRet ret = authManager.getSnsapiBaseinfo(weixinConf.getAppid(), weixinConf.getSecret(), code);
            String accessToken = authManager.getAccessToken();
            log.info("[ 获取user微信信息-> accessToken >{}]：", accessToken);
            if (ret != null && !StringUtils.isBlank(ret.getOpenid()) && !StringUtils.isBlank(accessToken)) {
                userInfo = authManager.getUserInfo(ret.getOpenid(), accessToken);
                log.info("[ 获取user微信信息-> userInfo >{}]：", JSON.toJSONString(userInfo));
            }
        } catch (Exception e) {
            log.error("[ 微信详情接口 ]请求异常：", e);
            return WebServiceMessage.failure("微信详情接口异常!");
        }
        return WebServiceMessage.success(userInfo);
    }

    @ApiOperation(value = "提现获取验证码接口", notes = "1、判断号码是否绑定；\n2、往用户绑定手机发送一个验证码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @RequestMapping(value = "/getSmsByCode", method = RequestMethod.GET)
    public WebServiceMessage getSmsByCode(HttpServletRequest request,
                                          @ApiParam(value = "手机号码") @RequestParam(value = "phone", required = true) String phone) throws Exception {
        return accountsService.getSmsByCode(phone);
    }

    @ApiOperation(value = "提现获取验证码接口 授权微信后 切换支付宝调用", notes = "1、判断号码是否绑定；\n2、往用户绑定手机发送一个验证码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })
    @RequestMapping(value = "/getAccountSmsCode", method = RequestMethod.GET)
    public WebServiceMessage getAccountSmsCode(@ApiParam(value = "uid") @RequestParam(value = "uid") Long uid,
                                               @ApiParam(value = "手机号码") @RequestParam(value = "phone") String phone) throws Exception {
        return accountsService.getAccountSmsCode(uid, phone);
    }

    @ApiOperation(value = "公众号提现验证码接口", notes = "公众号提现验证码接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserWithdrawVO.class)
    })
    @RequestMapping(value = "/getSmsCodeByWithdrawInfo", method = RequestMethod.POST)
    public WebServiceMessage getSmsCodeByWithdrawInfo(@ApiParam(value = "短信验证码") @RequestParam(value = "code") String code,
                                                      @ApiParam(value = "手机号码") @RequestParam(value = "phone") String phone) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.getSmsCodeByWithdrawInfo(phone, code));
    }

    @ApiOperation(value = "校验是否绑定微信", notes = "校验是否绑定微信")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserWithdrawVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/checkBindWx", method = RequestMethod.GET)
    public WebServiceMessage checkBindWx(@ApiParam(value = "微信unionid") @RequestParam(value = "unionId") String unionId) throws WebServiceException {
        UserWithdrawVO cashVo = withDrawService.checkBindWx(unionId);
        return WebServiceMessage.success(cashVo);
    }

    @ApiOperation(value = "校验是否绑定支付宝", notes = "校验是否绑定支付宝")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserWithdrawVO.class),
    })
    @RequestMapping(value = "/checkBindAliPay", method = RequestMethod.GET)
    public WebServiceMessage checkBindAliPay(@ApiParam(value = "uid") @RequestParam(value = "uid") Long uid) throws WebServiceException {
        UserWithdrawVO cashVo = withDrawService.checkBindAliPay(uid);
        return WebServiceMessage.success(cashVo);
    }

    @ApiOperation(value = "公众号钻石提现接口", notes = "根据类型（支付宝、微信）对用户提现方式保存。")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/noPublicWithDrawCash", method = RequestMethod.POST)
    public WebServiceMessage noPublicWithDrawCash(@ApiParam(value = "提现项目id") @RequestParam(value = "pid") String pid,
                                                  @ApiParam(value = "1，微信；2，支付宝") @RequestParam(value = "type") int type,
                                                  @ApiParam(value = "微信名") @RequestParam(value = "weixinName",
                                                          required = false) String weixinName,
                                                  @ApiParam(value = "微信openId") @RequestParam(value = "openId",
                                                          required = false) String openId,
                                                  @ApiParam(value = "提现类型 1、钻石") @RequestParam(value = "withdrawType"
                                                          , defaultValue = "1") int withdrawType,
                                                  @ApiParam(value = "支付宝账号") @RequestParam(value = "alipayAccount",
                                                          required = false) String alipayAccount,
                                                  @ApiParam(value = "支付宝真实姓名") @RequestParam(value = "realName",
                                                          required = false) String realName,
                                                  @ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                                  @ApiParam(value = "token") @RequestParam(value = "token") String token) throws Exception {
        WithDrawCashVO cashVo = withDrawService.noPublicWithDrawCash(uid, type, withdrawType, openId, weixinName,
                alipayAccount, realName, pid, token);
        return WebServiceMessage.success(cashVo);
    }

    @ApiOperation(value = "兑换金币", notes = "兑换金币")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/exchangeGold", method = RequestMethod.POST)
    public WebServiceMessage exchangeGold(@ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                          @ApiParam(value = "兑换数") @RequestParam(value = "exchangeNum") Double exchangeNum,
                                          @ApiParam(value = "类型 1、钻石") @RequestParam(value = "type") int type,
                                          @ApiParam(value = "token") @RequestParam(value = "token") String token) throws Exception {
        WithDrawCashVO cashVo = exchangeService.exchangeGold(uid, exchangeNum, type, token);
        return WebServiceMessage.success(cashVo);
    }

    /**
     * 返回提现列表
     *
     * @return WebServiceMessage
     */
    @RequestMapping(value = "/findWithdrawal", method = RequestMethod.GET)
    public WebServiceMessage findWithdrawal() {
        return WebServiceMessage.success(withDrawService.listAllCashProds());
    }

    // ------------------------- 提现接口(2020-09-21) -------------------------
    @ApiOperation(value = "(新)公众号提现获取提现信息接口", notes = "公众号提现获取提现信息接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserWithdrawVO.class)
    })
    @RequestMapping(value = "/getWithdrawInfo", method = RequestMethod.POST)
    public WebServiceMessage getWithdrawInfo(@ApiParam(value = "账号") @RequestParam(value = "userName") String userName,
                                             @ApiParam(value = "密码") @RequestParam(value = "password") String password) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.getWithdrawInfo(userName, password));
    }

    @ApiOperation(value = "(新)兑换金币", notes = "(新)兑换金币")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/exchangeGoldCoin", method = RequestMethod.POST)
    public WebServiceMessage exchangeGoldCoin(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid,
                                              @ApiParam(value = "兑换数") @RequestParam(value = "exchangeNum") Double exchangeNum,
                                              @ApiParam(value = "类型 1.钻石") @RequestParam(value = "type") int type,
                                              @ApiParam(value = "用户手机号") @RequestParam(value = "phone") String phone,
                                              @ApiParam(value = "短信验证码") @RequestParam(value = "code") String code,
                                              @ApiParam(value = "校验令牌") @RequestParam(value = "token") String token) throws Exception {
        WithDrawCashVO cashVo = exchangeService.exchangeGoldCoin(uid, exchangeNum, type, phone, code, token);
        return WebServiceMessage.success(cashVo);
    }

    @ApiOperation(value = "获取金融账户信息", notes = "获取金融账户信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserWithdrawVO.class),
    })
    @RequestMapping(value = "/getFinancialAccount", method = RequestMethod.GET)
    public WebServiceMessage getFinancialAccount(@ApiParam(value = "用户UID") @RequestParam(value = "uid") Long uid) throws WebServiceException {
        FinancialAccountVO financialAccountVO = withDrawService.getAccount(uid);
        return WebServiceMessage.success(financialAccountVO);
    }

    @ApiOperation(value = "公众号绑定账号并提现钻石接口", notes = "根据类型（支付宝、银行卡）对用户提现方式保存")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "/bindAndWithdraw", method = RequestMethod.POST)
    public WebServiceMessage bindFinancialAccountAndDiamondsWithdraw(@ApiParam(value = "提现选项ID") @RequestParam(value = "pid") String pid,
                                                                     @ApiParam(value = "1.微信; 2.支付宝; 3.银行卡") @RequestParam(value = "type") int type,
                                                                     @ApiParam(value = "提现钻石数量") @RequestParam(value = "diamondNum")Integer diamondNum,
                                                  @ApiParam(value = "提现账号") @RequestParam(value = "account",
                                                          required = false) String account,
                                                                     @ApiParam(value = "提现账号名称") @RequestParam(value = "accountName",
                                                          required = false) String accountName,
                                                                     @ApiParam(value = "手机号码") @RequestParam(value = "phone") String phone,
                                                                     @ApiParam(value = "短信验证码") @RequestParam(value = "code") String code,
                                                                     @ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                                                     @ApiParam(value = "token") @RequestParam(value = "token") String token) throws Exception {
        WithDrawCashVO cashVo = withDrawService.bindAndWithdraw(uid, type, diamondNum, account, accountName, pid, token, phone, code);
        return WebServiceMessage.success(cashVo);
    }
}
