//
//  YPHomeCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"

@interface YPHomeCore : YPBaseCore


//======================new home ui=================================
- (void)requestHomeTag;             //请求首页tag
- (void)requestRoomAllTag;          //请求所有房间tag
/*请求首页hot数据
 state:0 结束头部刷新。  1 结束底部熟悉
 */
- (void)requestHomeHotDataState:(int)state page:(int)page;
/*请求首页others数据
 state:0 结束头部刷新。  1 结束底部熟悉
 type: 标签类型
 */
- (void)requestHomeCommonData:(int)type state:(int)state page:(int)page;

- (void)requestHomeOtherMenuData;

//统计是否首次启动
- (NSNumber *)checkIsFirstLaunchApp;

/**
 首页更多推荐
 @param type 1.首页推荐位 2、更多推荐页
 */
- (void)getHomeGetindexWithPage:(NSInteger)page
                       pageSize:(NSInteger)pageSize
                           type:(NSInteger)type;

/**
 请求首页关注推荐房间数据
 */
- (void)getHomeAttentionRecommendData;

/**
 请求首页我关注的房间数据
 */
- (void)getMyAttentionDataWithPage:(NSInteger)page count:(NSInteger)count;

/**
 请求首页推荐的用户
 */
- (void)getHomeRecommendUserListData;

/**
 请求关注房间
 */
- (void)setupAttentionRoom:(NSInteger)roomId
                   success:(void (^)(void))success
                   failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 移除关注房间
 */
- (void)removeAttentionRoom:(NSString *)roomId
                    success:(void (^)(void))success
                    failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 获取首页banner
 */
- (void)getHomeBannerList;

@end
