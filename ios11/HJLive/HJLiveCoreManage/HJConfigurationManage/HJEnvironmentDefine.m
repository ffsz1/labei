//
//  HJEnvironmentDefine.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NSString *const JX_AES_ENCRYPT_KEY = @"09051825305fd819"; ///< AES加密Key
NSString *const JX_AES_ENCRYPT_IV = @"2dba43c93e7884b9";  ///< AES加密Iv

NSString *const JX_AES_DECRYPT_KEY = @"09051825305fd819"; ///< AES解密Key
NSString *const JX_AES_DECRYPT_IV = @"2dba43c93e7884b9";  ///< AES加密Iv

// Oauth
NSString *const JX_PWD_ENCRYPT_KEY = @"1ea53d260ecf11e7b56e00163e046a26"; ///< 密码Key

NSString *const JX_REQUEST_PARAMETER_ENCRYPT_KEY_RELEASE = @"ffe442276e74e2cf5167a2ee83773327"; ///< 请求参数Salt 1.0.0 正式服

//cb0eafcd7ff173bf38c7bc20953be431
NSString *const JX_REQUEST_PARAMETER_ENCRYPT_KEY_DEBUG = @"ffe442276e74e2cf5167a2ee83773327"; ///< 请求参数Salt 1.0.0 测试服

// 主机域名

//api.haijiaolive.cn
//NSString *const JX_HOST_URL_DEBUG = @"http://api.haijiaolive.cn";// rehuo 调试域名


//NSString *const JX_HOST_URL_DEBUG = @"http://lbapi.haijiaoxingqiu.cn:80"; ///< test
//NSString *const JX_HOST_URL_RELEASE = @"http://lbapi.haijiaoxingqiu.cn:80"; ///< distribute
//
//NSString *const JX_IM_WSS_SOCKET_URL_DEBUG = @"ws://lbim.haijiaoxingqiu.cn/"; ///< 测试服
//NSString *const JX_IM_WS_SOCKET_URL_DEBUG = @"ws://lbim.haijiaoxingqiu.cn/"; ///< 测试服
//
//NSString *const JX_IM_WS_SOCKET_URL_RELEASE = @"ws://lbim.haijiaoxingqiu.cn/"; ///< 正式服
//NSString *const JX_IM_WSS_SOCKET_URL_RELEASE = @"ws://lbim.haijiaoxingqiu.cn/"; ///< 正式服

NSString *const JX_HOST_URL_DEBUG = @"http://47.114.130.210:80"; ///< test
NSString *const JX_HOST_URL_RELEASE = @"http://47.114.130.210:80"; ///< distribute
//
NSString *const JX_IM_WSS_SOCKET_URL_DEBUG = @"ws://47.114.130.210:3006/"; ///< 测试服
NSString *const JX_IM_WS_SOCKET_URL_DEBUG = @"ws://47.114.130.210:3006/"; ///< 测试服

NSString *const JX_IM_WS_SOCKET_URL_RELEASE = @"ws://47.114.130.210:3006/"; ///< 正式服
NSString *const JX_IM_WSS_SOCKET_URL_RELEASE = @"ws://47.114.130.210:3006/"; ///< 正式服







// 七牛
//NSString *const JX_QN_AK_KEY = @"Ga8_g4VPgcsxJUen_r4wLxgH6R1QVBsjilheuefC"; ///< 七牛AK Key
//NSString *const JX_QN_SK_KEY = @"3UVIlCeQOes6ttbTL1bKCsTQrRLojFDPUktBqa_X"; ///< 七牛NK Key
//NSString *const JX_QN_BUCKET_NAME = @"wxv2019"; ///< 七牛buncket名
NSString *const JX_QN_AK_KEY = @"UW1BkYbOl8Zc8HEW6LcFVKAD7mGmtNykFrC4NMn5"; ///< 七牛AK Key
NSString *const JX_QN_SK_KEY = @"8ucBdPccEz90eCQmZ8nRHW0gYvEz3KGrIJWTzSF5"; ///< 七牛NK Key
NSString *const JX_QN_BUCKET_NAME = @"niun"; ///< 七牛buncket名

// 图片
//NSString *const JX_IMAGE_HOST_URL = @"http://q1rfo38x4.bkt.clouddn.com"; ///< 图片地址
NSString *const JX_IMAGE_HOST_URL = @"http://pic.pasuce.com/"; ///< 图片地址 pic.haijiaoxingqiu.cn


NSString *const JX_IMAGE_DEFAULT_AVATAR_URL = @"https://pic.hnyueqiang.com/default_head.png"; ///< 默认头像地址

// WEB
NSString *const JX_WEB_USER_AGENT_NAME = @"fyAppIos"; ///< User Agent

// We Chat
//NSString *const JX_WE_CHAT_APP_ID = @"wx9d25ad1bd451b69f"; ///< We Chat AppId
//NSString *const JX_WE_CHAT_APP_SECRET = @"63553889b00a8ec69fb075e71002fb16"; ///< We Chat AppSecret
//NSString *const JX_WE_CHAT_APP_ID = @"wx8f5179ce13b4114a"; ///< We Chat AppId
//NSString *const JX_WE_CHAT_APP_SECRET = @"fd40ca92c96906ff5b5e491f946cfba2"; ///< We Chat AppSecret
NSString *const JX_WE_CHAT_APP_ID = @"0"; ///< We Chat AppId
NSString *const JX_WE_CHAT_APP_SECRET = @"0"; ///< We Chat AppSecret


//NSString *const JX_QQ_APP_ID = @"1108246537"; ///< Q Q AppId
//NSString *const JX_QQ_APP_SECRET = @"W8mYlg2oaJ3kcy9a"; ///< Q Q AppSecret
//NSString *const JX_QQ_APP_ID = @"1105898337"; ///< Q Q AppId
//NSString *const JX_QQ_APP_SECRET = @"pC5VbCdTqQbIXUhz"; ///< Q Q AppSecret
NSString *const JX_QQ_APP_ID = @"0"; ///< Q Q AppId
NSString *const JX_QQ_APP_SECRET = @"0"; ///< Q Q AppSecret
NSString *const JX_QQ_SEXADECIMAL = @"0";


// 极光推送
NSString *const JX_JPUSH_APP_KEY = @"45e0affd380067ae96b1f7bf"; ///< 极光推送 AppKey

// 友盟
NSString *const JX_UMENG_APP_KEY = @"5d8ddc854ca3578d30000a17"; ///< 友盟 AppKey

// Zego
NSInteger const JX_ZEGO_APP_ID = 197844898; ///< Zego AppId
Byte JX_ZEGO_APP_SIGN[] = {0x04,0xf7,0x21,0xbf,0x98,0x12,0xb0,0x75,0x85,0x46,0x51,0x61,0x0c,0x65,0x4b,0x14,0xfa,0x8c,0xdc,0xd4,0x2b,0xbc,0x81,0xa4,0x32,0x98,0x5d,0xbe,0xe8,0xe3,0x14,0x35}; ///< Zego 签名

// 声网
NSString *const JX_AGORA_APP_ID = @"50bc04de14be43f7abeeb956b41c3545"; ///< 声网 AppId

// 易盾
NSString *const JX_NTES_VERIFY_CAPTCHA_ID = @"81a6c13f0502440bbefd18069058e993"; ///< 易盾 captchaId

// 云信
//NSString *const JX_NIM_APP_KEY = @"71d38984f36a4e3fe60eb6230e3cab5e"; ///<云信AppKey
NSString *const JX_NIM_APP_KEY = @"a09fff8642460fecd9fdb899bcba114c"; ///< 云信AppKey rhot
//App Secret
//6c979ac40f7c
