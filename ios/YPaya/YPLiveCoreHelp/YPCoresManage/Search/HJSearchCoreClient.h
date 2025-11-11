//
//  SearchCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HJSearchCoreClient <NSObject>
@optional
- (void)onUserSearchSuccess:(NSArray *)results;
- (void)onUserSearchFailth:(NSString *)message;
- (void)onSearchSuccess:(NSArray *)arr;
- (void)onSearchFailth:(NSString *)message;
@end
