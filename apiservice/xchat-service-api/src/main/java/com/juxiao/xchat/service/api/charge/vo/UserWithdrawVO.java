package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chris
 * @Title:
 * @date 2019-05-07
 * @time 15:01
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWithdrawVO {
    /**
     * 用户UID
     */
    private Long uid;
    /**
     * 海角号
     */
    private Long erbanNo;
    /**
     * 用户昵称
     */
    private String nick;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 钻石数量
     */
    private Double diamondNum;
    /**
     * 金币数量
     */
    private Double goldNum;
    /**
     * 是否绑定支付宝
     */
    private Boolean isBindAlipay;
    /**
     * 支付宝账号
     */
    private String alipayAccount;
    /**
     * 支付宝账号名
     */
    private String alipayAccountName;
    /**
     * 银行卡号
     */
    private String bankCard;
    /**
     * 银行卡账户姓名
     */
    private String bankCardName;
    /**
     * 授权令牌
     */
    private String token;
}