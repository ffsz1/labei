//
//  YPQQTool.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPQQTool.h"

#import "YPAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "NSObject+YYModel.h"

@interface YPQQTool ()

@property (nonatomic, strong) TencentOAuth *oauth;
@property (nonatomic, strong) YPQqUserInfo *qqInfo;

@end

@implementation YPQQTool

- (void)logout {
    [self.oauth logout:self];
    _oauth = nil;
    _qqInfo = nil;
}

- (void)loginWithQQ {
    self.oauth.authMode = kAuthModeClientSideToken;
    [self.oauth authorize:[self getPermissions] inSafari:NO];
}

- (NSMutableArray *)getPermissions
{
    NSMutableArray * g_permissions = [[NSMutableArray alloc] initWithObjects:kOPEN_PERMISSION_GET_USER_INFO,
                                      kOPEN_PERMISSION_GET_SIMPLE_USER_INFO,
                                      kOPEN_PERMISSION_ADD_ALBUM,
                                      kOPEN_PERMISSION_ADD_ONE_BLOG,
                                      kOPEN_PERMISSION_ADD_SHARE,
                                      kOPEN_PERMISSION_ADD_TOPIC,
                                      kOPEN_PERMISSION_CHECK_PAGE_FANS,
                                      kOPEN_PERMISSION_GET_INFO,
                                      kOPEN_PERMISSION_GET_OTHER_INFO,
                                      kOPEN_PERMISSION_LIST_ALBUM,
                                      kOPEN_PERMISSION_UPLOAD_PIC,
                                      kOPEN_PERMISSION_GET_VIP_INFO,
                                      kOPEN_PERMISSION_GET_VIP_RICH_INFO, nil];
    
    return g_permissions;
}

#pragma mark - TencentLoginDelegate
/**
 * 登录成功后的回调
 */
- (void)tencentDidLogin {
    [self.oauth getUserInfo];
}

/**
 * 登录失败后的回调
 * \param cancelled 代表用户是否主动退出登录
 */
- (void)tencentDidNotLogin:(BOOL)cancelled {
    if (cancelled) {
        NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginCancel), thirdPartLoginCancel);
    }
    else {
        NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
    }
}

/**
 * 登录时网络有问题的回调
 */
- (void)tencentDidNotNetWork {
    NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
}

#pragma mark - TencentSessionDelegate
/**
 * 退出登录的回调
 */
- (void)tencentDidLogout {
}

- (void)getUserInfoResponse:(APIResponse*)response
{
    NSDictionary *message = response.jsonResponse;
    YPQqUserInfo *info = [YPQqUserInfo yy_modelWithJSON:message];
    info.openID = [self.oauth.openId copy];
    info.token = [self.oauth.accessToken copy];
    self.qqInfo = info;
    
    GetCore(YPAuthCoreHelp).qqInfo = info;
    GetCore(YPAuthCoreHelp).info = nil;
    
    [GetCore(YPAuthCoreHelp) loginWithOpenID:info.openID andUnionID:nil access_token:info.token andType:XCThirdPartLoginQQ];
}

#pragma mark - QQApiInterfaceDelegate
/**
 处理来至QQ的响应
 */
- (void)onResp:(QQBaseResp *)resp {
    if ([resp.class isSubclassOfClass: [SendMessageToQQResp class]]) {  //QQ分享回应
        
        SendMessageToQQResp *msg = (SendMessageToQQResp *)resp;
        if ([msg.result integerValue] == 0) {
            if (self.didShareSuccess) {
                self.didShareSuccess();
            }
        }
        else {
            if (self.didShareFailed) {
                self.didShareFailed();
            }
        }
    }
}


#pragma mark - share
- (void)shareToQQFriendsWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr {
    //将内容分享到qq
    if (!_oauth) {
        [self oauth];
    }
    QQApiSendResultCode sent = [QQApiInterface sendReq:[self getMessageToQQReqWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr]];
    NSLog(@"%d",sent);
}

- (void)shareToQQQzoneWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr {
    //将内容分享到qzone
    if (!_oauth) {
        [self oauth];
    }
    QQApiSendResultCode sent = [QQApiInterface SendReqToQZone:[self getMessageToQQReqWithTitle:title description:description photoUrlStr:photoUrlStr urlStr:urlStr]];
}

- (SendMessageToQQReq *)getMessageToQQReqWithTitle:(NSString *)title description:(NSString *)description photoUrlStr:(NSString *)photoUrlStr urlStr:(NSString *)urlStr {
    QQApiNewsObject *newsObj = [QQApiNewsObject
                                objectWithURL:[NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]
                                title:title
                                description:description
                                previewImageURL:[NSURL URLWithString:[photoUrlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]];
    SendMessageToQQReq *req = [SendMessageToQQReq reqWithContent:newsObj];
    return req;
}

#pragma mark - setter && getter
- (TencentOAuth *)oauth {
    if (!_oauth) {
        NSString *appid = JX_QQ_APP_ID;
        _oauth = [[TencentOAuth alloc] initWithAppId:appid
                                         andDelegate:self];
    }
    return _oauth;
}

@end
