//
//  YPHttpRequestHelper+Share.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Share.h"
#import "YPAuthCoreHelp.h"

@implementation YPHttpRequestHelper (Share)

/**
  分享成功后调用
  
  @param shareType 分享类型，1微信好友，2微信朋友圈，3QQ好友，4QQ空间
  @param sharePageId 分享页面，1直播间，2H5
  @param targetUid 如果被分享房间，传被分享房间UID
  @param success 成功
  @param failure 失败
  */
+ (void)postShareSuccessWithShareType:(NSInteger)shareType sharePageId:(NSInteger)sharePageId targetUid:(NSInteger)targetUid success:(void (^)(NSString *packetNum))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    
    NSString *method = @"usershare/save";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(shareType) forKey:@"shareType"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(sharePageId) forKey:@"sharePageId"];
    if (sharePageId > 0) {
        [params setObject:@(targetUid) forKey:@"targetUid"];
    }
    
    [YPHttpRequestHelper POST:method params:params success:^(id data) {
        success(data[@"packetNum"]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode,message);
    }];
    
    
}

@end
