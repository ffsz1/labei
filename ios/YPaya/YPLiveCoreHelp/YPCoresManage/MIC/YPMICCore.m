//
//  YPMICCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMICCore.h"
#import "HJMICCoreClient.h"
#import "YPHttpRequestHelper+MICCore.h"
#import "YPMICUserInfo.h"
#import "NSObject+YYModel.h"

@implementation YPMICCore

- (void)getRandomUserList {
    [YPHttpRequestHelper getSoundMatchCharmUserWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[YPMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getSoundMatchCharmUserSuccessWithList:), getSoundMatchCharmUserSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getSoundMatchCharmUserFailthWithMessage:), getSoundMatchCharmUserFailthWithMessage:message);
    }];
}

//获取魅力用户列表
- (void)getCharmUserList {
    [YPHttpRequestHelper getCharmUserListWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[YPMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getCharmUserListSuccessWithList:), getCharmUserListSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getCharmUserListFailthWithMessage:), getCharmUserListFailthWithMessage:message);
    }];
}

- (void)getLinkPool {
    [YPHttpRequestHelper getLinkPoolWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[YPMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getLinkPoolListSuccessWithList:), getLinkPoolListSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getLinkPoolListFailthWithMessage:), getLinkPoolListFailthWithMessage:message);
    }];
}

- (void)getMICLinkUser {
    [YPHttpRequestHelper getMICLinkUserSuccess:^(NSDictionary *userInfo) {
        YPMICUserInfo *buffer = [YPMICUserInfo yy_modelWithJSON:userInfo];
        NotifyCoreClient(HJMICCoreClient, @selector(getMICLinkUserSuccess:), getMICLinkUserSuccess:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getMICLinkUserFailthWithMessage:), getMICLinkUserFailthWithMessage:message);
    }];
}

@end
