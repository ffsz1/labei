//
//  HJHttpRequestHelper+Home.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJHttpRequestHelper+Home.h"
#import "NSObject+YYModel.h"
#import "HJHomePageInfo.h"
#import "HJBannerInfo.h"
#import "HJHomeTag.h"
#import "HJHomeRankingInfo.h"
#import "HJAuthCoreHelp.h"
#import "YYUtility.h"
#import "PurseCore.h"
#import "HJVersionCoreHelp.h"
#import "HJHomeIcons.h"
#import "HJHomePeipeiModel.h"
#import "HJHomeMengxinModel.h"
#import "HJSendGoldModel.h"

@implementation HJHttpRequestHelper (Home)

//金币转赠权限校验
+ (void)requestChangeGivegoldCheckWithSuccess:(void (^)(BOOL isHas))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    NSString *method = @"change/givegoldcheck";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = JX_STR_AVOID_nil([GetCore(HJAuthCoreHelp) getTicket]);
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        BOOL isHas = [data boolValue];
        success(isHas);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//金币转赠
+ (void)requestChangeGoldTogold:(UserID)goldNum recvUid:(UserID)recvUid
                  success:(void (^)(HJSendGoldModel *model))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
    NSString *method = @"change/gold2gold";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    NSString *ticket = JX_STR_AVOID_nil([GetCore(HJAuthCoreHelp) getTicket]);
       [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(goldNum) forKey:@"goldNum"];
     [params setObject:@(recvUid) forKey:@"recvUid"];
    
    [params setObject:[NSNumber numberWithString:uid] forKey:@"sendUid"];
     [params setObject:uid forKey:@"uid"];

    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
       
        HJSendGoldModel* model = [HJSendGoldModel yy_modelWithDictionary:data];
        success(model);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

+ (void)requestBannerList:(void (^)(NSArray *list))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure
{
//    NSString *method = @"banner/list";
    NSString *method = @"home/getIndexTopBanner";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    [params setObject:uid forKey:@"uid"];
//    [params setObject:@"mm" forKey:@"app"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *bannerInfos = [NSArray yy_modelArrayWithClass:[HJBannerInfo class] json:data];
        success(bannerInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}
//
//
//+(void)requestRankingList:(void (^)(NSMutableDictionary *))success
//                  failure:(void (^)(NSNumber *resCode, NSString *message))failure{
//    NSString *method = @"allrank/homeV2";
//    [HttpRequestHelper GET:method params:nil success:^(id data) {
//        
//        NSArray *nobleList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"nobleList"]];
//        NSArray *starList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"starList"]];
//        NSArray *roomList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"roomList"]];
//        
//        NSMutableDictionary * result = [NSMutableDictionary dictionary];
//        if (nobleList.count > 0 && nobleList != nil) {
//            [result setObject:nobleList forKey:@"nobleList"];
//        }
//        
//        if (starList.count > 0 && starList != nil) {
//            [result setObject:starList forKey:@"starList"];
//        }
//        
//        if (roomList.count > 0 && roomList != nil) {
//            [result setObject:roomList forKey:@"roomList"];
//        }
//        
//        success(result);
//    } failure:^(NSNumber *resCode, NSString *message) {
//        if (failure) {
//            failure(resCode, message);
//        }
//    }];
//}


//======================new=================================

//请求首页tag数据
+ (void)requestHomeTag:(void (^)(NSArray *))success
               failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/tag/top";
    
    [HJHttpRequestHelper POST:method params:nil success:^(id data) {
//        NSLog(@"%@",data);
        NSArray *homeTags = [NSArray yy_modelArrayWithClass:[HJHomeTag class] json:data];
        success(homeTags);

    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}
 //请求房间所有tag数据
+ (void)requestRoomAllTag:(void (^)(NSArray *))success
                  failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/tag/all";
    
    [HJHttpRequestHelper POST:method params:nil success:^(id data) {
//        NSLog(@"%@",data);
        NSArray *romeTags = [NSArray yy_modelArrayWithClass:[HJHomeTag class] json:data];
        success(romeTags);
    } failure:^(NSNumber *resCode, NSString *message) {
//        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}

+ (void)requestHomeOtherMenuDataSuccess:(void (^)(NSArray *))success
                                failure:(void (^)(NSNumber *, NSString *))failure {
    NSString *method = @"room/tag/v2/classification";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setValue:GetCore(HJAuthCoreHelp).getUid forKey:@"uid"];
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *romeTags = [NSArray yy_modelArrayWithClass:[HJHomeTag class] json:data];
        success(romeTags);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//请求首页common数据
+ (void)requestHomeCommonData:(int)type state:(int)state page:(int)page
                      success:(void (^)(NSArray *list,int type))success
                      failure:(void (^)(NSNumber *, NSString *))failure{
    NSString *method = @"home/v2/tagindex";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(type) forKey:@"tagId"];
    [params setObject:@(page) forKey:@"pageNum"];
    [params setObject:@(25) forKey:@"pageSize"];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
//        NSLog(@"%@",data);
        NSArray *commonPageInfos = [NSArray yy_modelArrayWithClass:[HJHomePageInfo class] json:data];
        success(commonPageInfos,type);
    } failure:^(NSNumber *resCode, NSString *message) {
//        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}

//萌新数据
+ (void)requestHomeNewUsersDataWithPage:(int)page
                        success:(void (^)(NSArray *))success
                        failure:(void (^)(NSNumber *, NSString *))failure{
    
    NSString *method = @"home/newUsers";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(page) forKey:@"pageNum"];
    [params setObject:@(25) forKey:@"pageSize"];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    [params setObject:uid forKey:@"uid"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
      
         NSArray * mengxinList = [NSArray yy_modelArrayWithClass:[HJHomeMengxinModel class] json:data];
        success(mengxinList);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}
//陪陪数据
+ (void)requestHomeBestCompaniesDataWithPage:(int)page gender:(NSInteger)gender
                        success:(void (^)(NSArray *))success
                        failure:(void (^)(NSNumber *, NSString *))failure{
    
    NSString *method = @"home/bestCompanies";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(page) forKey:@"pageNum"];
    [params setObject:@(25) forKey:@"pageSize"];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    [params setObject:uid forKey:@"uid"];
     [params setObject:@(gender) forKey:@"gender"];

    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        
      
         NSArray * peipeiList = [NSArray yy_modelArrayWithClass:[HJHomePeipeiModel class] json:data];
        success(peipeiList);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}


//请求首页hot数据
+ (void)requestHomeHotDataState:(int)state page:(int)page
                        success:(void (^)(NSMutableDictionary *))success
                        failure:(void (^)(NSNumber *, NSString *))failure{
    
    NSString *method = @"home/v2/hotindex";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:@(page) forKey:@"pageNum"];
    [params setObject:@(25) forKey:@"pageSize"];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    [params setObject:uid forKey:@"uid"];
 
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        NSLog(@"%@",data);
        NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
        //banners
        NSArray *bannerInfos = [NSArray yy_modelArrayWithClass:[HJBannerInfo class] json:data[@"banners"]];
        if (bannerInfos.count>0) {
            [dictionary setObject:bannerInfos forKey:@"banners"];
        }
        //rankHome
        NSDictionary *rankHome = [self handleRankData:data[@"rankHome"]];
        if (rankHome.allKeys>0) {
            [dictionary setObject:rankHome forKey:@"rankHome"];
        }
        
        NSArray *homeIconList = [self handleHomeIconsData:data[@"homeIcons"]];
        if (homeIconList.count > 0) {
            [dictionary setObject:homeIconList forKey:@"homeIcons"];
        }
        
        //hot
        NSArray *hotList = [self handleHotList:data[@"hotRooms"]];
        if (hotList.count>0) {
             [dictionary setObject:hotList forKey:@"hotRooms"];
        }
        //common
        NSArray *commonList = [self handleCommonListData:data[@"listRoom"]];
        if (commonList.count>0) {
             [dictionary setObject:commonList forKey:@"listRoom"];
        }
        
        NSArray *agreeRecommendRooms = [self handleCommonListData:data[@"agreeRecommendRooms"]];
        if (agreeRecommendRooms.count>0) {
            [dictionary setObject:agreeRecommendRooms forKey:@"agreeRecommendRooms"];
        }
        
        NSArray *greenList = [self handleCommonListData:data[@"listGreenRoom"]];
        if (greenList.count>0) {
            [dictionary setObject:greenList forKey:@"listGreenRoom"];
        }
        
        NSArray *recommendRooms = [self handleCommonListData:data[@"recommendRooms"]];
        if (recommendRooms.count>0) {
            [dictionary setObject:recommendRooms forKey:@"recommendRooms"];
        }
        
        NSArray *roomTagList = [NSArray yy_modelArrayWithClass:[HJHomeTag class] json:data[@"roomTagList"]];
        if (roomTagList.count>0) {
            [dictionary setObject:roomTagList forKey:@"roomTagList"];
        }
        
        success(dictionary);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}


/**
 首页更多推荐
 */
+ (void)getHomeGetindexWithPage:(NSInteger)page
                       pageSize:(NSInteger)pageSize
                        success:(void (^)(NSArray *dataArr))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"home/v2/getindex";
//    NSString *method = @"home/v3/getindex";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"pageNum"] = @(page);
    params[@"pageSize"] = @(pageSize);
    params[@"uid"] = [GetCore(HJAuthCoreHelp) getUid];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *dataArr = [self handleHotList:data];
        if (success) {
            success(dataArr);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode,message);
        }
    }];
}

/**
 请求关注推荐列表
 */
+ (void)requestAttentionRecommendListWithSuccess:(void (^)(NSArray <HJHomeRoomInfo *>* data))success failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"room/getRoomRecommendList";
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *ticket = JX_STR_AVOID_nil([GetCore(HJAuthCoreHelp) getTicket]);
    NSString *uid = JX_STR_AVOID_nil([GetCore(HJAuthCoreHelp) getUid]);
    [params setObject:uid forKey:@"uid"];
    [params setObject:ticket forKey:@"ticket"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *arr = [NSArray yy_modelArrayWithClass:[HJHomeRoomInfo class] json:data];
        success([arr mutableCopy]);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

//请求关注数列表据
+ (void)requestAttentionWithPageNum:(NSInteger)pageNum
                              count:(NSInteger)count
                            success:(void (^)(NSArray <HJHomeRoomInfo *>* data))success
                            failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"room/attention/getRoomAttentionByUid";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    [params setObject:@(pageNum) forKey:@"pageNum"];
    [params setObject:@(count) forKey:@"pageSize"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *homeRoomInfoArray = [NSArray yy_modelArrayWithClass:[HJHomeRoomInfo class] json:data];
        NSArray *dataArray = data;
        
        for (NSInteger i = 0; i < homeRoomInfoArray.count; i++) {
            HJHomeRoomInfo *model = homeRoomInfoArray[i];
            model.ID = dataArray[i][@"id"];
            model.isSelect = NO;
        }
        
        success(homeRoomInfoArray);
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
        failure(resCode, message);
        
    }];
    
}

//获取推荐用户列表
+ (void)requestRecommendUserListSuccess:(void (^)(NSArray <UserInfo *> *list))success
                                failure:(void (^)(NSNumber *resCode, NSString *message))failure {
    NSString *method = @"user/getRecommendUsers";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        NSArray *UserInfoArray = [NSArray yy_modelArrayWithClass:[UserInfo class] json:data];
        if (success) {
            success(UserInfoArray);
        }
    } failure:^(NSNumber *resCode, NSString *message) {
        if (failure) {
            failure(resCode, message);
        }
    }];
}

//请求足迹数据
+ (void)requestFootprintSuccess:(void (^)(NSArray <HJHomeRoomInfo *>* data))success failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    
    NSString *method = @"room/attention/getFootprint";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    
    [HJHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *homeRoomInfoArray = [NSArray yy_modelArrayWithClass:[HJHomeRoomInfo class] json:data];
        NSArray *dataArray = data;
        
        
        for (NSInteger i = 0; i < homeRoomInfoArray.count; i++) {
            
            HJHomeRoomInfo *model = homeRoomInfoArray[i];
            model.ID = dataArray[i][@"id"];
        }
        
        success(homeRoomInfoArray);
        
        //        NSLog(@"%@",data);
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
        NSLog(@"%@",message);
        
    }];
    
    
}

//关注房间接口
+ (void)requestRoomAttention:(NSInteger)roomId
                     success:(void (^)(id data))success
                     failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/attention/attentions";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    [params setObject:[NSNumber numberWithInteger:roomId] forKey:@"roomId"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}

//检查是否已关注
+ (void)requestCheckRoomAttention:(NSInteger)roomId
                          success:(void (^)(id data))success
                          failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    NSString *method = @"room/attention/checkAttentions";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    [params setObject:[NSNumber numberWithInteger:roomId] forKey:@"roomId"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}

//删除关注房间接口   rooms:多个roomID逗号隔开
+ (void)requestDelRoomAttention:(NSString *)rooms
                        success:(void (^)(id data))success
                        failure:(void (^)(NSNumber *resCode, NSString *message))failure{
    
    
    if (rooms.length == 0) {
        return;
    }
    
    NSString *method = @"room/attention/delAttentions";
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    [params setObject:[GetCore(HJAuthCoreHelp) getUid] forKey:@"uid"];
    [params setObject:[GetCore(HJAuthCoreHelp) getTicket] forKey:@"ticket"];
    [params setObject:rooms forKey:@"roomId"];
    
    [HJHttpRequestHelper POST:method params:params success:^(id data) {
        success(data);
    } failure:^(NSNumber *resCode, NSString *message) {
        NSLog(@"%@",message);
        failure(resCode, message);
    }];
}


#pragma mark - private method
+ (NSArray *)handleHotList:(id)data{
    NSArray * hotList = [NSArray yy_modelArrayWithClass:[HJHomePageInfo class] json:data];
    return hotList;
}

+ (NSArray *)handleCommonListData:(id)data{
    NSArray * commonList = [NSArray yy_modelArrayWithClass:[HJHomePageInfo class] json:data];
    return commonList;
}

+ (NSArray *)handleHomeIconsData:(id)data{
    NSArray * homeIconData = [NSArray yy_modelArrayWithClass:[HJHomeIcons class] json:data];
    return homeIconData;
}

+ (NSDictionary *)handleRankData:(id)data{
    NSArray *nobleList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"nobleList"]];
    NSArray *starList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"starList"]];
    NSArray *roomList = [NSArray yy_modelArrayWithClass:[HJHomeRankingInfo class] json:data[@"roomList"]];
    
    NSMutableDictionary * rankHome = [NSMutableDictionary dictionary];
    if (nobleList.count > 0 && nobleList != nil) {
        [rankHome setObject:nobleList forKey:@"nobleList"];
    }
    if (starList.count > 0 && starList != nil) {
        [rankHome setObject:starList forKey:@"starList"];
    }
    if (roomList.count > 0 && roomList != nil) {
        [rankHome setObject:roomList forKey:@"roomList"];
    }
    return [rankHome copy];
}
@end
