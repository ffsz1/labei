//
//  YPHttpRequestHelper+Bill.m
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHttpRequestHelper+Bill.h"
#import "YPAuthCoreHelp.h"
#import "NSObject+YYModel.h"
#import "YPGiftBillInfo.h"
#import "YPChatBillInfo.h"
#import "YPRechargeBillInfo.h"
#import "YPWithDrawlBillInfo.h"
#import "YPRedBillInfo.h"
#import "NSString+JsonToDic.h"
#import "YYUtility.h"

#import "PLTimeUtil.h"

@implementation YPHttpRequestHelper (Bill)



+ (void)getGiftInBillWithPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize Success:(void (^)(NSMutableArray *,NSMutableArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    //NSString *method = @"personbill/list";
    NSString * method = @"billrecord/get";
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:ticket forKey:@"ticket"];
    
    NSInteger type = 2;
    [params setObject:@(type) forKey:@"type"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    [params setObject:@(pageSize) forKey:@"pageSize"];
    
    if (time > 0) {
        [params setObject:@(time) forKey:@"date"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *billList = data[@"billList"];
       // NSInteger pageCount = [data[@"pageCount"] integerValue];
        NSMutableArray *keysList = [NSMutableArray array];
        NSMutableArray *dataArr = [NSMutableArray array];
        for (NSDictionary *item in billList) {
            NSArray *subKeys = [item allKeys];
            NSString *key = subKeys[0];
            NSArray *oneDayArr = [NSArray yy_modelArrayWithClass:[YPGiftBillInfo class] json:[item objectForKey:key]];
            NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
            [keysList addObject:finallyKey];
            [dataArr addObject:oneDayArr];
        }
        success(dataArr, keysList);
        
//        NSArray *keys = [data allKeys];
//        NSMutableArray *arr = [NSMutableArray array];
//        NSMutableArray *finallyKeys = [NSMutableArray array];
//        for (NSString *key in keys) {
//            if (![key isEqualToString:@"pageCount"]) {
//                NSArray *tempArr = [NSArray yy_modelArrayWithClass:[YPGiftBillInfo class] json:[data objectForKey:key]];
//                [arr addObject:[tempArr mutableCopy]];
//                NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
//                [finallyKeys addObject:finallyKey];
//            }
//        }
//
//
//        success(arr,finallyKeys);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}


+ (void)getGiftOutBillWithPageNo:(NSInteger)pageNo time:(NSInteger)time pageSize:(NSInteger)pageSize Success:(void (^)(NSMutableArray *, NSMutableArray *))success failure:(void (^)(NSNumber *, NSString *))failure {
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    //NSString *method = @"personbill/list";
    NSString * method = @"billrecord/get";
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSInteger type = 1;
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    //[params setObject:@(time) forKey:@"date"];
    [params setObject:@(pageSize) forKey:@"pageSize"];
    
    if (time > 0) {
        [params setObject:@(time) forKey:@"date"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        NSArray *billList = data[@"billList"];
        //NSInteger pageCount = [data[@"pageCount"] integerValue];
        NSMutableArray *keysList = [NSMutableArray array];
        NSMutableArray *dataArr = [NSMutableArray array];
        for (NSDictionary *item in billList) {
            NSArray *subKeys = [item allKeys];
            NSString *key = subKeys[0];
            NSArray *oneDayArr = [NSArray yy_modelArrayWithClass:[YPGiftBillInfo class] json:[item objectForKey:key]];
            NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
            [keysList addObject:finallyKey];
            [dataArr addObject:oneDayArr];
        }
        success(dataArr, keysList);
        
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

/**
 密聊记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getChatBillWithPageNo:(NSInteger)pageNo
                         time:(NSInteger)time
                     pageSize:(NSInteger)pageSize
                      Success:(void (^)(NSMutableArray *, NSMutableArray *))success
                      failure:(void (^)(NSNumber *, NSString *))failure {
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    //NSString *method = @"personbill/list";
    NSString * method = @"billrecord/get";
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSInteger type = 3;
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:ticket forKey:@"ticket"];
    [params setObject:@(type) forKey:@"type"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    [params setObject:@(pageSize) forKey:@"pageSize"];
    
    if (time > 0) {
        [params setObject:@(time) forKey:@"date"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *billList = data[@"billList"];
        NSInteger pageCount = [data[@"pageCount"] integerValue];
        NSMutableArray *keysList = [NSMutableArray array];
        NSMutableArray *dataArr = [NSMutableArray array];
        for (NSDictionary *item in billList) {
            NSArray *subKeys = [item allKeys];
            NSString *key = subKeys[0];
            NSArray *oneDayArr = [NSArray yy_modelArrayWithClass:[YPChatBillInfo class] json:[item objectForKey:key]];
            NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
            [keysList addObject:finallyKey];
            [dataArr addObject:oneDayArr];
        }
        success(dataArr, keysList);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}

/**
 充值记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getRechargeBillWithPageNo:(NSInteger)pageNo
                             time:(NSInteger)time
                         pageSize:(NSInteger)pageSize
                          Success:(void (^)(NSMutableArray *, NSMutableArray *))success
                          failure:(void (^)(NSNumber *, NSString *))failure {
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    //NSString *method = @"personbill/list";
    NSString *method = @"billrecord/get";
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:ticket forKey:@"ticket"];
    NSInteger type = 4;
    [params setObject:@(type) forKey:@"type"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    [params setObject:@(pageSize) forKey:@"pageSize"];
    
    if (time > 0) {
        [params setObject:@(time) forKey:@"date"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        
        NSArray *billList = data[@"billList"];
        NSInteger pageCount = [data[@"pageCount"] integerValue];
        NSMutableArray *keysList = [NSMutableArray array];
        NSMutableArray *dataArr = [NSMutableArray array];
        for (NSDictionary *item in billList) {
            NSArray *subKeys = [item allKeys];
            NSString *key = subKeys[0];
            NSArray *oneDayArr = [NSArray yy_modelArrayWithClass:[YPRechargeBillInfo class] json:[item objectForKey:key]];
            NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
            [keysList addObject:finallyKey];
            [dataArr addObject:oneDayArr];
        }
        success(dataArr, keysList);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}



/**
 提现记录
 
 @param pageNo 页码
 @param time 时间戳
 @param pageSize 每页大小
 @param success 成功回调
 @param failure 失败回调
 */
+ (void)getWithdrawBillWithPageNo:(NSInteger)pageNo
                             time:(NSInteger)time
                         pageSize:(NSInteger)pageSize
                          Success:(void (^)(NSMutableArray *, NSMutableArray *))success
                          failure:(void (^)(NSNumber *, NSString *))failure {
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    
    //NSString *method = @"personbill/list";
    NSString *method = @"billrecord/get";
    NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
    NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
    [params setObject:ticket forKey:@"ticket"];
    NSInteger type = 5;
    [params setObject:@(type) forKey:@"type"];
    [params setObject:uid forKey:@"uid"];
    [params setObject:@(pageNo) forKey:@"pageNo"];
    [params setObject:@(pageSize) forKey:@"pageSize"];
    
    if (time > 0) {
        [params setObject:@(time) forKey:@"date"];
    }
    
    [YPHttpRequestHelper GET:method params:params success:^(id data) {
        NSLog(@"%@",data);
        NSArray *billList = data[@"billList"];
        NSInteger pageCount = [data[@"pageCount"] integerValue];
        NSMutableArray *keysList = [NSMutableArray array];
        NSMutableArray *dataArr = [NSMutableArray array];
        for (NSDictionary *item in billList) {
            NSArray *subKeys = [item allKeys];
            NSString *key = subKeys[0];
            NSArray *oneDayArr = [NSArray yy_modelArrayWithClass:[YPRechargeBillInfo class] json:[item objectForKey:key]];
            NSString *finallyKey = [PLTimeUtil getDateWithYYMMDD:key];
            [keysList addObject:finallyKey];
            [dataArr addObject:oneDayArr];
        }
        success(dataArr, keysList);
    } failure:^(NSNumber *resCode, NSString *message) {
        failure(resCode, message);
    }];
}









@end
