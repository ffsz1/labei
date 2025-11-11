//
//  HJEnvironmentDefine.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

// 是否向外发布版本(1: 是, 0: 否)
// 外发版的时候需要改动，修改该值会影响到实验室、Crash日志等。
#define OFFICIAL_RELEASE 1



/*
 企业包
 
 appid=1001->正常
 appid=1002->企业包
 */
#define JX_APPID_VERSION @"1001"

typedef NS_ENUM(NSInteger, JXCryptAlgorithm) { ///< 加密/解密算法
    JXCryptAlgorithmUndefine  = 0,             ///< 未定义
    JXCryptAlgorithmAES       = 1,             ///< AES
    JXCryptAlgorithmDES       = 2,             ///< DES
};

typedef NS_ENUM(NSUInteger, BillType) {
    BillType_GiftIn, //礼物收入账单
    BillType_GiftOut, //礼物支出账单
    BillType_Recharge,//充值账单
    BillType_Withdraw,//提现
    BillType_RedWithdraw,//红包提现
    BillType_daiChong,//代充记录
};

FOUNDATION_EXTERN NSString *const JX_AES_ENCRYPT_KEY; ///< AES加密Key
FOUNDATION_EXTERN NSString *const JX_AES_ENCRYPT_IV;  ///< AES加密Iv

FOUNDATION_EXTERN NSString *const JX_AES_DECRYPT_KEY; ///< AES解密Key
FOUNDATION_EXTERN NSString *const JX_AES_DECRYPT_IV;  ///< AES加密Iv

// Oauth
FOUNDATION_EXTERN NSString *const JX_PWD_ENCRYPT_KEY; ///< 密码Key
FOUNDATION_EXTERN NSString *const JX_REQUEST_PARAMETER_ENCRYPT_KEY_RELEASE; ///< 请求参数Salt 1.0.0
FOUNDATION_EXTERN NSString *const JX_REQUEST_PARAMETER_ENCRYPT_KEY_DEBUG; ///< 请求参数Salt 1.0.0 测试服

// 主机域名
FOUNDATION_EXTERN NSString *const JX_HOST_URL_DEBUG; ///< test
FOUNDATION_EXTERN NSString *const JX_HOST_URL_RELEASE; ///< distribute

// IM域名
FOUNDATION_EXTERN NSString *const JX_IM_WSS_SOCKET_URL_DEBUG; ///< 测试服
FOUNDATION_EXTERN NSString *const JX_IM_WS_SOCKET_URL_DEBUG; ///< 测试服

FOUNDATION_EXTERN NSString *const JX_IM_WSS_SOCKET_URL_RELEASE; ///< 正式服
FOUNDATION_EXTERN NSString *const JX_IM_WS_SOCKET_URL_RELEASE; ///< 正式服

// 七牛
FOUNDATION_EXTERN NSString *const JX_QN_AK_KEY; ///< 七牛AK Key
FOUNDATION_EXTERN NSString *const JX_QN_SK_KEY; ///< 七牛NK Key
FOUNDATION_EXTERN NSString *const JX_QN_BUCKET_NAME; ///< 七牛buncket名

// 图片
FOUNDATION_EXTERN NSString *const JX_IMAGE_HOST_URL; ///< 图片地址
FOUNDATION_EXTERN NSString *const JX_IMAGE_DEFAULT_AVATAR_URL; ///< 默认头像地址

// WEB
FOUNDATION_EXTERN NSString *const JX_WEB_USER_AGENT_NAME; ///< User Agent

// We Chat
FOUNDATION_EXTERN NSString *const JX_WE_CHAT_APP_ID; ///< We Chat AppId
FOUNDATION_EXTERN NSString *const JX_WE_CHAT_APP_SECRET; ///< We Chat AppSecret

// Q Q
FOUNDATION_EXTERN NSString *const JX_QQ_APP_ID; ///< Q Q AppId
FOUNDATION_EXTERN NSString *const JX_QQ_APP_SECRET; ///< Q Q AppSecret
FOUNDATION_EXTERN NSString *const JX_QQ_SEXADECIMAL;

// 极光推送
FOUNDATION_EXTERN NSString *const JX_JPUSH_APP_KEY; ///< 极光推送 AppKey

// 友盟
FOUNDATION_EXTERN NSString *const JX_UMENG_APP_KEY; ///< 友盟 AppKey

// Zego
FOUNDATION_EXTERN NSInteger const JX_ZEGO_APP_ID; ///< Zego AppId
FOUNDATION_EXTERN Byte JX_ZEGO_APP_SIGN[]; ///< Zego 签名

// 声网
FOUNDATION_EXTERN NSString *const JX_AGORA_APP_ID; ///< 声网 AppId

// 易盾
FOUNDATION_EXTERN NSString *const JX_NTES_VERIFY_CAPTCHA_ID; ///< 易盾 captchaId

// 云信
FOUNDATION_EXTERN NSString *const JX_NIM_APP_KEY; ///< 云信AppKey
