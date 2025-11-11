//
//  SearchCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSearchCoreHelp.h"
#import "HJSearchCoreClient.h"
#import "YPHttpRequestHelper+Search.h"

@implementation YPSearchCoreHelp

- (void)searchWithKey:(NSString *)key {
    [YPHttpRequestHelper requestInfoWithKey:key Success:^(NSArray *list) {
        NotifyCoreClient(HJSearchCoreClient, @selector(onSearchSuccess:), onSearchSuccess:list);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJSearchCoreClient, @selector(onSearchFailth:), onSearchFailth:message);
    }];
}

- (void)searchUser:(NSString *)key pageNo:(NSInteger)pageNo pageSize:(NSInteger)pageSize
{
    [YPHttpRequestHelper searchUser:key pageNo:pageNo pageSize:pageSize success:^(NSArray *userInfos) {
        NotifyCoreClient(HJSearchCoreClient, @selector(onUserSearchSuccess:), onUserSearchSuccess:userInfos);
    } failure:^(NSNumber *resCode, NSString *message) {
        NotifyCoreClient(HJSearchCoreClient, @selector(onUserSearchFailth:), onUserSearchFailth:message);
    }];
}

- (void)searchRoom:(NSString *)key
{
    
}

@end
