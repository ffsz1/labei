package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.charge.dto.WithDrawCashProdDTO;
import com.juxiao.xchat.service.api.charge.vo.*;

import java.util.List;

/**
 * 钻石提现业务接口
 *
 * @class: WithDrawService.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface WithDrawService {
    /**
     * 获取提现用户信息(提现页面)
     *
     * @param uid 用户UID
     * @return
     * @throws WebServiceException
     */
    WithDrawVO getWithDraw(Long uid) throws WebServiceException;

    /**
     * 绑定支付宝账号
     *
     * @param uid               用户UID
     * @param aliPayAccount     支付宝账号
     * @param aliPayAccountName 支付宝账号名称
     * @param code              验证码
     * @return
     * @throws WebServiceException
     */
    AliPayBoundVO boundAliPay(Long uid, String aliPayAccount, String aliPayAccountName, String code) throws WebServiceException;

    /**
     * 查询提现列表
     *
     * @return
     */
    List<WithDrawCashProdDTO> listAllCashProds();

    /**
     * 绑定提现账户
     *
     * @param uid         用户UID
     * @param diamondId   提现选项ID
     * @param diamondNum  提现钻石数量
     * @param account     账户号码
     * @param accountName 账户名称
     * @param accountType 账户类型 [ 1.支付宝; 2.银行卡 ]
     * @param appVersion  App版本
     * @param phone       手机号码
     * @param passwordSecond     二级密码
     * @return
     * @throws WebServiceException
     */
    BindAccountVO bindAccount(Long uid, String diamondId, Integer diamondNum, String account, String accountName,
                              Integer accountType,
                              String appVersion, String phone, String passwordSecond) throws Exception;

    /**
     * 获取所有提现账户
     *
     * @param uid 用户UID
     * @return
     * @throws WebServiceException
     */
    FinancialAccountVO getAccount(Long uid) throws WebServiceException;

    /**
     * 验证绑定第三方短信
     *
     * @param uid  用户UID
     * @param code 验证码
     * @throws WebServiceException
     */
    void checkCode(Long uid, String code) throws WebServiceException;

    /**
     * 绑定第三方账户
     *
     * @param uid         用户UID
     * @param openId      第三方OpenId
     * @param unionId     第三方UnionId
     * @param accessToken 访问Token令牌
     * @param type        绑定类型 [1.微信; 2.QQ]
     * @param app         AppId
     * @param os          操作系统
     * @return 返回绑定结果
     * @throws WebServiceException
     */
    int boundThird(Long uid, String openId, String unionId, String accessToken, int type, String app, String os) throws WebServiceException;

    /**
     * 解绑第三方账户
     *
     * @param uid  用户UID
     * @param type 解绑类型 [1.微信; 2.QQ]
     * @return 返回解绑结果
     * @throws WebServiceException
     */
    int unBoundThird(Long uid, int type) throws WebServiceException;

    /**
     * 客户端钻石提现 (安卓)
     *
     * @param uid        用户UID
     * @param pid        提现选项ID
     * @param diamondNum 提现钻石数量
     * @param type       提现类型 [1.微信; 2.支付宝; 3.银行卡]
     * @return
     */
    WithDrawCashVO withDrawCash(Long uid, String pid, Integer diamondNum, int type) throws Exception;

    /**
     * douyu钻石提现
     *
     * @param uid
     * @param pid
     * @return
     */
    WithDrawCashVO withDrawCashNowxali(Long uid, String pid, int type) throws Exception;

    /**
     * 根据手机及短信验证码获取钻石提现信息
     *
     * @param phone 手机号
     * @param code  短信验证码
     * @return UserWithdrawVO
     * @throws WebServiceException WebServiceException
     */
    UserWithdrawVO getSmsCodeByWithdrawInfo(String phone, String code) throws WebServiceException;

    /**
     * 根据海角号和密码获取钻石提现信息
     *
     * @param userName 账号
     * @param password 密码
     * @return
     * @throws WebServiceException
     */
    UserWithdrawVO getWithdrawInfo(String userName, String password) throws WebServiceException;

    /**
     * 检查是否绑定微信
     *
     * @param unionId UnionId
     * @return UserWithdrawVO
     * @throws WebServiceException WebServiceException
     */
    UserWithdrawVO checkBindWx(String unionId) throws WebServiceException;

    /**
     * 检测是否绑定支付宝
     *
     * @param uid 用户UID
     * @return UserWithdrawVO
     * @throws WebServiceException WebServiceException
     */
    UserWithdrawVO checkBindAliPay(Long uid) throws WebServiceException;

    /**
     * 公众号钻石提现
     *
     * @param uid           uid
     * @param type          提现方式 [1.微信；2.支付宝; 3.银行卡]
     * @param withdrawType  提现类型 [1.钻石]
     * @param openId        微信OpenId
     * @param weixinName    微信真实姓名
     * @param alipayAccount 支付宝账号
     * @param realName      支付宝真实姓名
     * @param pid           提现选项ID
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    WithDrawCashVO noPublicWithDrawCash(Long uid, int type, int withdrawType, String openId, String weixinName,
                                        String alipayAccount, String realName, String pid, String token) throws WebServiceException;

    /**
     * 公众号绑定账号并提现钻石接口
     *
     * @param uid         用户UID
     * @param type        提现类型: [1.微信; 2.支付宝; 3.银行卡]
     * @param account     提现账户
     * @param accountName 提现账户名
     * @param pid         提现选项ID
     * @param token       授权校验令牌
     * @return
     * @throws WebServiceException
     */
    WithDrawCashVO bindAndWithdraw(Long uid, int type, Integer diamondNum, String account, String accountName,
                                   String pid, String token, String phone, String code) throws WebServiceException;

    /**
     * 提现账户绑定
     *
     * @param uid         用户ID
     * @param account     账户号码
     * @param accountName 账户名称
     * @param accountType 账户类型 [ 1.支付宝; 2.银行卡 ]
     * @param appVersion  App版本
     * @return
     * @throws WebServiceException
     */
    WebServiceMessage resetUserAccount(Long uid, String account, String accountName, Integer accountType,
                                       String appVersion) throws WebServiceException;
}
