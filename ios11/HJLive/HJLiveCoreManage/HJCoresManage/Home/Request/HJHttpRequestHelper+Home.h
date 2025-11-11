//
//  HJHttpRequestHelper+Home.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper.h"
#import "HJHomeRoomInfo.h"
#import "HJSendGoldModel.h"
@class UserInfo;
@interface HJHttpRequestHelper (Home)
//金币转赠权限校验 life-
+ (void)requestChangeGivegoldCheckWithSuccess:(void (^)(BOOL ishas))success
                                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//转金币 life-
+ (void)requestChangeGoldTogold:(UserID)goldNum recvUid:(UserID)recvUid
success:(void (^)(HJSendGoldModel *model))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure;
///**
// 获取轻聊主页
//
// @param success 成功
// @param failure 失败
// */
//+ (void)requestChatListSuccess:(void (^)(NSArray *list))success failure:(void (^)(NSNumber *, NSString *))failure;
//
///**
// 获取轰趴主页
//
// @param success 成功
// @param failure 失败
// */
//+ (void)requestHomePartyListSuccess:(void (^)(NSArray *list))success
//                           failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//
///**
// 获取主页信息V2
//
// @param success 成功
// @param failure 失败
// */
//+ (void)requestMainHomeListSuccess:(void (^)(NSMutableDictionary *))success
//failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//
///**
// 获取主页信息
//
// @param success 成功
// @param failure 失败
// */
//+(void)requestHomeList:(void (^)(NSArray *))success
//                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//
/**
 获取banner

 @param success 成功
 @param failure 失败
 */
+(void)requestBannerList:(void (^)(NSArray *list))success
                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;

///**
// 获取排行榜信息
// 
// @param success 成功
// @param failure 失败
// */
//+(void)requestRankingList:(void (^)(NSMutableDictionary *))success
//                 failure:(void (^)(NSNumber *resCode, NSString *message))failure;




//======================new home ui=================================
/**
 请求首页tag数据
 */
+ (void)requestHomeTag:(void (^)(NSArray *))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 请求房间所有tag数据
 */
+ (void)requestRoomAllTag:(void (^)(NSArray *))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure;
//萌新数据
+ (void)requestHomeNewUsersDataWithPage:(int)page
                        success:(void (^)(NSArray *))success
                                failure:(void (^)(NSNumber *, NSString *))failure;
//陪陪数据
+ (void)requestHomeBestCompaniesDataWithPage:(int)page gender:(NSInteger)gender
success:(void (^)(NSArray *))success
failure:(void (^)(NSNumber *, NSString *))failure;
/**
 请求首页hot数据
 */
+ (void)requestHomeHotDataState:(int)state page:(int)page
                        success:(void (^)(NSMutableDictionary *))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 请求首页common数据
 */
+ (void)requestHomeCommonData:(int)type state:(int)state page:(int)page
                      success:(void (^)(NSArray *list,int type))success
                      failure:(void (^)(NSNumber *resCode, NSString *message))failure;

+ (void)requestHomeOtherMenuDataSuccess:(void (^)(NSArray *))success
                                failure:(void (^)(NSNumber *, NSString *))failure;

/**
 首页更多推荐
 */
+ (void)getHomeGetindexWithPage:(NSInteger)page
                       pageSize:(NSInteger)pageSize
                        success:(void (^)(NSArray *dataArr))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure;
/**
 请求足迹数据
 */
+ (void)requestFootprintSuccess:(void (^)(NSArray <HJHomeRoomInfo *>* data))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 请求关注推荐列表
 */
+ (void)requestAttentionRecommendListWithSuccess:(void (^)(NSArray <HJHomeRoomInfo *>* data))success failure:(void (^)(NSNumber *resCode, NSString *message))failure;

/**
 请求关注数列表据
 */
+ (void)requestAttentionWithPageNum:(NSInteger)pageNum
                              count:(NSInteger)count
                            success:(void (^)(NSArray <HJHomeRoomInfo *>* data))success
                            failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//获取推荐用户列表
+ (void)requestRecommendUserListSuccess:(void (^)(NSArray <UserInfo *> *list))success
                                failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//关注房间接口
+ (void)requestRoomAttention:(NSInteger)roomId
                     success:(void (^)(id data))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//检查是否已关注
+ (void)requestCheckRoomAttention:(NSInteger)roomId
                          success:(void (^)(id data))success
                          failure:(void (^)(NSNumber *resCode, NSString *message))failure;

//删除关注房间接口   rooms:多个roomID逗号隔开
+ (void)requestDelRoomAttention:(NSString *)rooms
                        success:(void (^)(id data))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure;

@end
