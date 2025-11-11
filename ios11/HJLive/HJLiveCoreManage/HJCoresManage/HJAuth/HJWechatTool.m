//
//  HJWechatTool.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJWechatTool.h"

#import "HJAuthCoreHelp.h"
#import "HJAuthCoreClient.h"
#import "NSObject+YYModel.h"

@interface HJWechatTool ()

@property (nonatomic, strong) HJWeChatUserInfo *wechtInfo;

@end

@implementation HJWechatTool


- (void)registerApp {
    [WXApi registerApp:JX_WE_CHAT_APP_ID];
}

- (void)loginWithWechat {
    //构造SendAuthReq结构体
    SendAuthReq* req =[[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"wx_oauth_authorization_state";
    //第三方向微信终端发送一个SendAuthReq消息结构
    [WXApi sendReq:req];
}

- (void)logout {
    _wechtInfo = nil;
}

- (void)shareToWechatFriendsWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl {
    WXWebpageObject *webpageObject = [WXWebpageObject object];
    webpageObject.webpageUrl = webpageUrl;
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[self compressImage:[self getImageFromURL:imageUrl] toByte:32765]];
    message.mediaObject = webpageObject;
    
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;
    [WXApi sendReq:req];
}

- (void)shareToWechatFriendsCircleWithTitle:(NSString *)title description:(NSString *)description webpageUrl:(NSString *)webpageUrl imageUrl:(NSString *)imageUrl {
    WXWebpageObject *webpageObject = [WXWebpageObject object];
    webpageObject.webpageUrl = webpageUrl;
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[self compressImage:[self getImageFromURL:imageUrl] toByte:32765]];
    message.mediaObject = webpageObject;
    
    SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneTimeline;
    [WXApi sendReq:req];
}

- (UIImage *)getImageFromURL:(NSString *)fileURL

{
    UIImage * result;
    
    NSData * data = [NSData dataWithContentsOfURL:[NSURL URLWithString:fileURL]];
    
    result = [UIImage imageWithData:data];
    
    return result;
}

#pragma mark - 压缩图片
- (UIImage *)compressImage:(UIImage *)image toByte:(NSUInteger)maxLength {
    // Compress by quality
    CGFloat compression = 1;
    NSData *data = UIImageJPEGRepresentation(image, compression);
    if (data.length < maxLength) return image;
    
    CGFloat max = 1;
    CGFloat min = 0;
    for (int i = 0; i < 6; ++i) {
        compression = (max + min) / 2;
        data = UIImageJPEGRepresentation(image, compression);
        if (data.length < maxLength * 0.9) {
            min = compression;
        } else if (data.length > maxLength) {
            max = compression;
        } else {
            break;
        }
    }
    UIImage *resultImage = [UIImage imageWithData:data];
    if (data.length < maxLength) return resultImage;
    
    // Compress by size
    NSUInteger lastDataLength = 0;
    while (data.length > maxLength && data.length != lastDataLength) {
        lastDataLength = data.length;
        CGFloat ratio = (CGFloat)maxLength / data.length;
        CGSize size = CGSizeMake((NSUInteger)(resultImage.size.width * sqrtf(ratio)),
                                 (NSUInteger)(resultImage.size.height * sqrtf(ratio))); // Use NSUInteger to prevent white blank
        UIGraphicsBeginImageContext(size);
        [resultImage drawInRect:CGRectMake(0, 0, size.width, size.height)];
        resultImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        data = UIImageJPEGRepresentation(resultImage, compression);
    }
    
    return resultImage;
}

#pragma mark - WXApiDelegate
- (void)onResp:(BaseResp *)resp {
    if([resp isKindOfClass:[SendAuthResp class]]) {
        SendAuthResp* authResp = (SendAuthResp*)resp;
        switch (resp.errCode) {
            case WXSuccess:
                [self wechatLoginDidSuccessWithCode:authResp.code];
                break;
            case WXErrCodeAuthDeny:
                NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
                break;
            case WXErrCodeUserCancel:
                NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginCancel), thirdPartLoginCancel);
            default:
                NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
                break;
        }
    }
    else if([resp isKindOfClass:[SendMessageToWXResp class]]){
        SendMessageToWXResp *req = (SendMessageToWXResp *)resp;
        //这里不再返回用户是否分享完成事件，即原先的cancel事件和success事件将统一为success事件
        if (req.errCode == 0) {
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

- (void)wechatLoginDidSuccessWithCode:(NSString *)code {
    
    @weakify(self);
    [HJHttpRequestHelper getWechtAccessTokenWithCode:code appId:JX_WE_CHAT_APP_ID appSecret:JX_WE_CHAT_APP_SECRET success:^(id data) {
        
        @strongify(self);
        NSDictionary *dic = (NSDictionary *)data;
        
        NSInteger errorCode = [dic[@"errcode"] integerValue];
        
        if (errorCode == 40029) {
            NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
        }
        else {
            NSString *access_token = [dic[@"access_token"] description];
            NSString *openid = [dic[@"openid"] description];
            NSString *unionid = [dic[@"unionid"] description];
            
            @weakify(self);
            [HJHttpRequestHelper getWechtUserInfoWithAccessToken:access_token openId:openid success:^(id userInfo) {
                
                @strongify(self);
                NSDictionary *userInfoDic = (NSDictionary *)userInfo;
                
                NSInteger userInfoErrorCode = [dic[@"errcode"] integerValue];
                
                if (userInfoErrorCode == 40003) {
                    NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
                }
                else {
                    
                    
                    self.wechtInfo = [HJWeChatUserInfo yy_modelWithDictionary:userInfoDic];
                    
                    
                    GetCore(HJAuthCoreHelp).qqInfo = nil;
                    GetCore(HJAuthCoreHelp).info = self.wechtInfo;
                    
                    [GetCore(HJAuthCoreHelp) loginWithOpenID:openid andUnionID:unionid access_token:access_token andType:XCThirdPartLoginWc];
                }
                
            } failure:^(NSURLSessionDataTask *userInfoTask, NSError *userInfoError) {
                NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
            }];
        }
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NotifyCoreClient(HJAuthCoreClient, @selector(thirdPartLoginFailth), thirdPartLoginFailth);
    }];
}


@end
