//
//  HJMICCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@class HJMICUserInfo;

@protocol HJMICCoreClient <NSObject>

@optional
- (void)getCharmUserListSuccessWithList:(NSArray *)list;
- (void)getCharmUserListFailthWithMessage:(NSString *)message;


- (void)getLinkPoolListSuccessWithList:(NSArray *)list;
- (void)getLinkPoolListFailthWithMessage:(NSString *)message;

- (void)getMICLinkUserSuccess:(HJMICUserInfo *)userInfo;
- (void)getMICLinkUserFailthWithMessage:(NSString *)message;

- (void)getSoundMatchCharmUserSuccessWithList:(NSArray *)list;
- (void)getSoundMatchCharmUserFailthWithMessage:(NSString *)message;


@end
