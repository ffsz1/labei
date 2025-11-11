//
//  HJMICCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMICCore.h"
#import "HJMICCoreClient.h"
#import "HJHttpRequestHelper+MICCore.h"
#import "HJMICUserInfo.h"
#import "NSObject+YYModel.h"

@implementation HJMICCore

- (void)getRandomUserList {
    [HJHttpRequestHelper getSoundMatchCharmUserWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[HJMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getSoundMatchCharmUserSuccessWithList:), getSoundMatchCharmUserSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getSoundMatchCharmUserFailthWithMessage:), getSoundMatchCharmUserFailthWithMessage:message);
    }];
}

//获取魅力用户列表
- (void)getCharmUserList {
    [HJHttpRequestHelper getCharmUserListWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[HJMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getCharmUserListSuccessWithList:), getCharmUserListSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getCharmUserListFailthWithMessage:), getCharmUserListFailthWithMessage:message);
    }];
}

- (void)getLinkPool {
    [HJHttpRequestHelper getLinkPoolWithSuccess:^(NSArray *list) {
        NSArray *buffer = [NSArray yy_modelArrayWithClass:[HJMICUserInfo class] json:list];
        NotifyCoreClient(HJMICCoreClient, @selector(getLinkPoolListSuccessWithList:), getLinkPoolListSuccessWithList:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getLinkPoolListFailthWithMessage:), getLinkPoolListFailthWithMessage:message);
    }];
}

- (void)getMICLinkUser {
    [HJHttpRequestHelper getMICLinkUserSuccess:^(NSDictionary *userInfo) {
        HJMICUserInfo *buffer = [HJMICUserInfo yy_modelWithJSON:userInfo];
        NotifyCoreClient(HJMICCoreClient, @selector(getMICLinkUserSuccess:), getMICLinkUserSuccess:buffer);
    } failure:^(NSNumber *code, NSString *message) {
        NotifyCoreClient(HJMICCoreClient, @selector(getMICLinkUserFailthWithMessage:), getMICLinkUserFailthWithMessage:message);
    }];
}

@end
