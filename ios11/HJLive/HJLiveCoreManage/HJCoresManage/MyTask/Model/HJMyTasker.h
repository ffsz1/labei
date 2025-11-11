//
//  HJMyTasker.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "HJMyTaskModel.h"

@interface HJMyTasker : NSObject

//每日任务
@property (nonatomic, strong) NSArray<HJMyTaskModel *> *daily;
//新手任务
@property (nonatomic, strong) NSArray<HJMyTaskModel *> *fresh;
//时间任务
@property (nonatomic, strong) NSArray<HJMyTaskModel *> *dailyTime;

@property (nonatomic, assign) NSInteger roomTime;

@end
