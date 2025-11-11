//
//  HJHomeCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//



#import <Foundation/Foundation.h>

@protocol HJHomeCoreClient <NSObject>

@optional



//network
- (void)networkReconnect:(NSInteger)tag;

//======================new home ui=================================
//请求首页tag
- (void)onRequestHomeTagListSuccess:(NSArray *)list;
- (void)onRequestHomeTagListFailth:(NSString *)msg;
//请求所有房间tag
- (void)requestRoomAllTagListSuccess:(NSArray *)list;
- (void)requestRoomAllTagListFailth:(NSString *)msg;
//请求首页hot数据
- (void)requestHomeHotDataListState:(int)state success:(NSMutableDictionary *)list;
- (void)requestHomeHotDataListState:(int)state failth:(NSString *)msg;
//请求首页others数据
- (void)requestHomeCommonDataListState:(int)state success:(NSArray *)list type:(int)type;
- (void)requestHomeCommonDataListState:(int)state failth:(NSString *)msg type:(int)type;

- (void)requestHomeOtherMenuDataSuccess:(NSArray *)data;
- (void)requestHomeOtherMenuDataFail:(int)state failth:(NSString *)msg;

- (void)notiIndex:(NSInteger)index;

// 首页推荐
- (void)getHomeGetindexDataSuccess:(NSArray *)data type:(NSInteger)type;
- (void)getHomeGetindexDataFail:(NSString *)msgt type:(NSInteger)type;

//首页关注推荐列表
- (void)getHomeAttentionRecommendDataSuccess:(NSArray *)data;
- (void)getHomeAttentionRecommendDataFail:(NSString *)msg;

//首页关注推荐列表
- (void)getMyAttentionDataSuccess:(NSArray *)data;
- (void)getMyAttentionDataFail:(NSString *)msg;

//首页banner获取
- (void)onRequestHomeBannerSuccess:(NSArray *)list;
- (void)onRequestHomeBannerFailth:(NSString *)msg;

//获取推荐用户
- (void)onRequestRecommendUserListSuccess:(NSArray *)userInfoList;
- (void)onRequestRecommendUserListFailure:(NSString *)message;

@end 
