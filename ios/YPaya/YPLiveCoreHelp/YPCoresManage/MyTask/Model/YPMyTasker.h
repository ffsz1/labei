//
//  YPMyTasker.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "YPMyTaskModel.h"

@interface YPMyTasker : NSObject

//每日任务
@property (nonatomic, strong) NSArray<YPMyTaskModel *> *daily;
//新手任务
@property (nonatomic, strong) NSArray<YPMyTaskModel *> *fresh;
//时间任务
@property (nonatomic, strong) NSArray<YPMyTaskModel *> *dailyTime;

@property (nonatomic, assign) NSInteger roomTime;

@end
