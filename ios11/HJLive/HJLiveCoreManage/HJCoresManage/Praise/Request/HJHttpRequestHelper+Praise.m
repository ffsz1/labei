//
//  HJHttpRequestHelper+Praise.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Praise.h"
#import "HJAuthCoreHelp.h"
#import "UserInfo.h"
#import "Attention.h"
#import "NSObject+YYModel.h"

@implementation HJHttpRequestHelper (Praise)

+ (void)praise:(UserID)paiseUid bePraisedUid:(UserID)bePraisedUid success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (paiseUid <= 0 || bePraisedUid <= 0) {
        return;
    }
    
    NSString *ticket = [GetCore(HJAuthCoreHelp) getTicket];
    
    NSString *method = @"fans/like";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(paiseUid) forKey:@"uid"];
    [params setObject:@(bePraisedUid) forKey:@"likedUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(1) forKey:@"type"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)cancel:(UserID)cancelUid beCanceledUid:(UserID)beCanceledUid success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (cancelUid <= 0 || beCanceledUid <= 0) {
        return;
    }
    
    NSString *ticket = GetCore(HJAuthCoreHelp).getTicket;
    
    NSString *method = @"fans/like";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(cancelUid) forKey:@"uid"];
    [params setObject:@(beCanceledUid) forKey:@"likedUid"];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(2) forKey:@"type"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void) deleteFriend:(UserID)deleteFriendUid beDeletedFriendUid:(UserID)beDeletedFriendUid
             success:(void (^)(void))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (deleteFriendUid <= 0 || beDeletedFriendUid <= 0) {
        return;
    }
    
    NSString *ticket = GetCore(HJAuthCoreHelp).getTicket;
    
    NSString *method = @"fans/fdelete";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(deleteFriendUid) forKey:@"uid"];
    [params setObject:@(beDeletedFriendUid) forKey:@"likedId"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        if (success) {
            success();
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void) requestAttentionList:(UserID)uid
                        state:(int)state
                         page:(int)page
                      success:(void (^)(NSArray *userIDs))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    if (uid <= 0) {
        return;
    }
    
    NSString *method = @"fans/following";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(25) forKey:@"pageSize"];
    [params setObject:@(page) forKey:@"pageNo"];
    
    NSString *ticket = GetCore(HJAuthCoreHelp).getTicket;

    [params setObject:ticket forKey:@"ticket"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *userInfos = [NSArray yy_modelArrayWithClass:[Attention class] json:data];
        if (success) {
            success(userInfos);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)isLike:(UserID)uid isLikeUid:(UserID)isLikeUid success:(void (^)(BOOL))success failure:(void (^)(NSNumber *, NSString *))failure
{
    if (uid <= 0) {
        return;
    }
    
    NSString *method = @"fans/islike";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(uid) forKey:@"uid"];
    [params setObject:@(isLikeUid) forKey:@"isLikeUid"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        if (![data isKindOfClass:[NSDictionary class]]) {
            BOOL isLike = ((NSNumber *)data).boolValue;
            if (success) {
                success(isLike);
            }
        }else {
            success(NO);
        }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//获取用户粉丝列表
+ (void)getFansListWithUid:(UserID)uid page:(NSInteger)page success:(void (^)(NSArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"fans/fanslist";
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:@(uid) forKey:@"uid"];
    if (page == 0) {
        [params setObject:@(1) forKey:@"pageNo"];
    }else {
        [params setObject:@(page) forKey:@"pageNo"];
    }
    [params setObject:@(25) forKey:@"pageSize"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        @try {
            NSArray *userInfos = [NSArray yy_modelArrayWithClass:[Attention class] json:data[@"fansList"]];
            if (success) {
                success(userInfos);
            }
        } @catch(NSException *e) {
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

@end
