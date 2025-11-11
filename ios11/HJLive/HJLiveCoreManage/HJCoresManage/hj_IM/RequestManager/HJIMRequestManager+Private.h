//
//  HJIMRequestManager+Private.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJIMRequestManager.h"

@interface HJIMRequestManager ()

@property (nonatomic, assign) BOOL repeatConnect;
@property (nonatomic, assign) NSTimeInterval repeatTimeInterval;
@property (nonatomic, strong) dispatch_source_t repeatTimer;

@property (nonatomic, copy) JXIMRequestSuccessHander loginSuccessHandler;
@property (nonatomic, copy) JXIMRequestFailureHander loginFailureHandler;
@property (nonatomic, strong) id loginParams;

@end
