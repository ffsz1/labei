//
//  HJActivityCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ActivityInfo.h"
#import "HJRedInfo.h"


@protocol HJActivityCoreClient <NSObject>
@optional
- (void)getActivityInfoSuccess;

- (void)getAllActivityInfoSuccess:(NSArray *)arr;

- (void)onReceiveP2PRedPacket:(HJRedInfo *)info;
@end
