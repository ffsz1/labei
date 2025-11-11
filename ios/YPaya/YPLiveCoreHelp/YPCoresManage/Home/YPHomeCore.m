//
//  YPHomeCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeCore.h"
#import "HJHomeCoreClient.h"
#import "YPHttpRequestHelper+Home.h"
#import <AFNetworkReachabilityManager.h>
#import "YPVersionCoreHelp.h"


@implementation YPHomeCore

- (instancetype)init
{
    self = [super init];
    if (self) {
        @weakify(self);
        [[AFNetworkReachabilityManager sharedManager]setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
            @strongify(self);
            switch (status) {
                case AFNetworkReachabilityStatusUnknown:
                    break;
                case AFNetworkReachabilityStatusNotReachable:
                    break;
                case AFNetworkReachabilityStatusReachableViaWWAN:
                    NotifyCoreClient(HJHomeCoreClient, @selector(networkReconnect:), networkReconnect:1);
                    break;
                case AFNetworkReachabilityStatusReachableViaWiFi:
                    NotifyCoreClient(HJHomeCoreClient, @selector(networkReconnect:), networkReconnect:2);
                    break;
                default:
                    break;
            }
        }];
    }
    return self;
}

- (NSNumber *)checkIsFirstLaunchApp {
    NSUserDefaults *detaults = [NSUserDefaults standardUserDefaults];
    if (![detaults objectForKey:@"FirstLaunchApp"]) {
        return @1;
    }
        BOOL tag = [detaults objectForKey:@"FirstLaunchApp"];
        return @(tag);
}


//======================new=================================
//请求首页tag
- (void)requestHomeTag{
    [YPHttpRequestHelper requestHomeTag:^(NSArray *list) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestHomeTagListSuccess:), onRequestHomeTagListSuccess:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestHomeTagListFailth:), onRequestHomeTagListFailth:message);
    }];
}

//请求所有房间tag
- (void)requestRoomAllTag{
    [YPHttpRequestHelper requestRoomAllTag:^(NSArray *list) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestRoomAllTagListSuccess:), requestRoomAllTagListSuccess:list);
    } failure:^(NSNumber *resCode, NSString *message) {
       NotifyCoreClient(HJHomeCoreClient, @selector(requestRoomAllTagListFailth:), requestRoomAllTagListFailth:message);
    }];
}

//请求首页hot数据
- (void)requestHomeHotDataState:(int)state page:(int)page{
    [YPHttpRequestHelper requestHomeHotDataState:(int)state page:(int)page success:^(NSMutableDictionary *dictionary) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeHotDataListState:success:), requestHomeHotDataListState:state success:dictionary);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeHotDataListState:failth:), requestHomeHotDataListState:state failth:message);
    }];
}

//请求首页others数据
- (void)requestHomeCommonData:(int)type state:(int)state page:(int)page{
    [YPHttpRequestHelper requestHomeCommonData:(int)type state:(int)state page:(int)page success:^(NSArray *list,int type) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeCommonDataListState:success:type:), requestHomeCommonDataListState:state success:list type:type);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeCommonDataListState:failth:type:), requestHomeCommonDataListState:state failth:message type:type);
    }];
}

//请求首页others列表数据
- (void)requestHomeOtherMenuData {
    [YPHttpRequestHelper requestHomeOtherMenuDataSuccess:^(NSArray *arr) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeOtherMenuDataSuccess:), requestHomeOtherMenuDataSuccess:arr);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(requestHomeOtherMenuDataFail:failth:), requestHomeOtherMenuDataFail:resCode failth:message);
    }];
}

/**
 首页更多推荐
 @param type 1.首页推荐位 2、更多推荐页
 */
- (void)getHomeGetindexWithPage:(NSInteger)page
                       pageSize:(NSInteger)pageSize
                           type:(NSInteger)type {
    
    [YPHttpRequestHelper getHomeGetindexWithPage:page pageSize:pageSize success:^(NSArray *dataArr) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getHomeGetindexDataSuccess:type:), getHomeGetindexDataSuccess:dataArr type:type);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getHomeGetindexDataFail:type:), getHomeGetindexDataFail:message type:type);
    }];
}

/**
 请求首页关注推荐房间数据
 */
- (void)getHomeAttentionRecommendData {
    [YPHttpRequestHelper requestAttentionRecommendListWithSuccess:^(NSArray<YPHomeRoomInfo *> *data) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getHomeAttentionRecommendDataSuccess:), getHomeAttentionRecommendDataSuccess:data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getHomeAttentionRecommendDataFail:), getHomeAttentionRecommendDataFail:message);
    }];
}

/**
 请求首页我关注的房间数据
 */
- (void)getMyAttentionDataWithPage:(NSInteger)page count:(NSInteger)count {
    [YPHttpRequestHelper requestAttentionWithPageNum:page count:count success:^(NSArray<YPHomeRoomInfo *> *data) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getMyAttentionDataSuccess:), getMyAttentionDataSuccess:data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(getMyAttentionDataFail:), getMyAttentionDataFail:message);
    }];
}

/**
 请求首页推荐的用户
 */
- (void)getHomeRecommendUserListData {
    [YPHttpRequestHelper requestRecommendUserListSuccess:^(NSArray<UserInfo *> *list) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestRecommendUserListSuccess:), onRequestRecommendUserListSuccess:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestRecommendUserListFailure:), onRequestRecommendUserListFailure:message);
    }];
}

/**
 请求关注房间
 */
- (void)setupAttentionRoom:(NSInteger)roomId
                   success:(void (^)(void))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    [YPHttpRequestHelper requestRoomAttention:roomId success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

/**
 移除关注房间
 */
- (void)removeAttentionRoom:(NSString *)roomId
                    success:(void (^)(void))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    [YPHttpRequestHelper requestDelRoomAttention:roomId success:^(id data) {
        !success ?: success();
    } failure:^(NSNumber *resCode, NSString *message) {
        !failure ?: failure(resCode, message);
    }];
}

/**
 获取首页banner
 */
- (void)getHomeBannerList {
    [YPHttpRequestHelper requestBannerList:^(NSArray *list) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestHomeBannerSuccess:), onRequestHomeBannerSuccess:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJHomeCoreClient, @selector(onRequestHomeBannerFailth:), onRequestHomeBannerFailth:message);
    }];
}



@end
